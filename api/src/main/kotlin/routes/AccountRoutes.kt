package com.routes

import com.constants.TimeConstants
import com.model.Account.Account
import com.model.Account.AccountLogin
import com.model.Account.AccountRegister
import com.model.Account.AccountWithPassword
import com.repository.AccountRepository
import com.utility.Argon
import com.utility.fromDocument
import com.utility.generateTokens
import com.utility.isEmail
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.Document
import java.util.*

fun Route.accountRoutes(repository: AccountRepository) {
    route("") {
        post("/login") {
            val user = call.receive<AccountLogin>()
            if (!isEmail(user.email)) {
                return@post call.respondText(
                    text = "Login unsuccessful",
                    status = HttpStatusCode.Unauthorized
                )
            }
            val account: Document = repository.findByEmail(user.email)
                ?: return@post call.respondText(
                    text = "Login unsuccessful",
                    status = HttpStatusCode.Unauthorized
                )
            if (!Argon.verify(account["password"].toString(), user.password)) {
                return@post call.respondText(
                    text = "Login unsuccessful",
                    status = HttpStatusCode.Unauthorized
                )
            }
            println("Login successful")
            call.respond(generateTokens(account["_id"].toString(), environment))
        }
        post("/register") {
            val account = call.receive<AccountRegister>()
            if (repository.findByEmail(account.email) != null) {
                return@post call.respondText(
                    text = "Email already taken",
                    status = HttpStatusCode.Unauthorized
                )
            }
            val accountToSave =
                AccountWithPassword(
                    account.name,
                    account.email,
                    Argon.hash(account.password),
                    0,
                    emptyList(),
                    emptyList()
                )
            val insertedId = repository.register(accountToSave)
            call.respond(generateTokens(insertedId, environment))
        }
        authenticate {
            post("/refresh-token") {
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val refreshToken = call.request.headers["Authorization"]?.removePrefix("Bearer ").toString()
                val isRefreshToken = principal.payload.getClaim("refresh_token").asString() == "CURRENT_REFRESH_TOKEN"
                val id = principal.payload.getClaim("_id").asString()
                if (!isRefreshToken) {
                    return@post call.respondText(
                        text = "Incorrect token",
                        status = HttpStatusCode.Unauthorized
                    )
                }
                val tokens = generateTokens(id, environment)
                val expiresAt = principal.payload.expiresAt
                val currentDate = Date()
                val diff = expiresAt.time - currentDate.time
                if (diff > TimeConstants.FIVE_DAYS) {
                    tokens["refreshToken"] = refreshToken
                }
                call.respond(tokens)
            }
            route("/account") {
                get("") {
                    println("Account get")
                    val principal = call.principal<JWTPrincipal>() ?: return@get
                    val id = principal.payload.getClaim("_id").asString()
                    repository.findById(id)?.let {
                        call.respond(fromDocument(it, Account.serializer()))
                    } ?: call.respond(
                        HttpStatusCode.NotFound,
                        hashMapOf("response" to "Account not found")
                    )
                }
                get("/{id?}") {
                    val id = call.parameters["id"] ?: return@get call.respondText(
                        text = "Missing id",
                        status = HttpStatusCode.BadRequest
                    )
                    repository.findById(id)?.let {
                        call.respond(fromDocument(it, Account.serializer()))
                    } ?: call.respond(
                        HttpStatusCode.NotFound,
                        hashMapOf("response" to "Account not found")
                    )
                }
                withAccessRight(1) {
                    delete("/{id?}") {
                        val id = call.parameters["id"] ?: return@delete call.respondText(
                            text = "Missing id",
                            status = HttpStatusCode.BadRequest
                        )
                        repository.delete(id)?.let {
                            call.respondText("Account removed successfully")
                        } ?: call.respondText("Account not found", status = HttpStatusCode.NotFound)
                    }
                    put("/{id?}") {
                        val id = call.parameters["id"] ?: return@put call.respondText(
                            text = "Missing id",
                            status = HttpStatusCode.BadRequest
                        )
                        repository.update(id, call.receive())?.let {
                            call.respondText("Account updated")
                        } ?: call.respondText("Account not found", status = HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
}
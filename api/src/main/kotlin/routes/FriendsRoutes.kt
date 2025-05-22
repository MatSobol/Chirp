package com.routes

import com.model.Account.Account
import com.model.Account.Friend
import com.repository.AccountRepository
import com.utility.fromDocument
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.friendsRoutes(repository: AccountRepository) {
    authenticate {
        route("/friends") {
            post("/{id?}") {
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val id = principal.payload.getClaim("_id").asString()
                val friendID = call.parameters["id"] ?: return@post call.respondText(
                    text = "Missing id",
                    status = HttpStatusCode.BadRequest
                )
                val accountDocument = repository.findById(id) ?: return@post call.respondText(
                    text = "Issue with token",
                    status = HttpStatusCode.BadRequest
                )
                val account = fromDocument(accountDocument, Account.serializer())
                val friend = account.friendRequests.filter { it.id == friendID }[0]
                val friendSelf = Friend(id, account.name)
                repository.removeFriendRequest(id, friendID)
                repository.addFriend(id, friend)
                repository.addFriend(friendID, friendSelf)
                call.respondText("Added Friend")
            }
            post("/request/{email?}") {
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val id = principal.payload.getClaim("_id").asString()
                val email = call.parameters["email"] ?: return@post call.respondText(
                    text = "Missing email",
                    status = HttpStatusCode.BadRequest
                )
                val accountDocument = repository.findById(id) ?: return@post call.respondText(
                    text = "Issue with token",
                    status = HttpStatusCode.BadRequest
                )
                val account = fromDocument(accountDocument, Account.serializer())
                if (account.email == email) return@post call.respondText(
                    text = "Can't send to self",
                    status = HttpStatusCode.BadRequest
                )
                val friend = Friend(id, account.name)
                val result = repository.addFriendRequest(email, friend)
                if (result.modifiedCount > 0) {
                    call.respondText("Send request")
                } else {
                    call.respondText(
                        text = "No user found",
                        status = HttpStatusCode.BadRequest
                    )
                }
            }
            delete("/request/{id?}") {
                val principal = call.principal<JWTPrincipal>() ?: return@delete
                val id = principal.payload.getClaim("_id").asString()
                val friendID = call.parameters["id"] ?: return@delete call.respondText(
                    text = "Missing id",
                    status = HttpStatusCode.BadRequest
                )
                repository.removeFriendRequest(id, friendID)
                call.respondText("Friend request deleted")
            }
        }

    }

}
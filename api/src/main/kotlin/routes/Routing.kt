package com.routes

import com.mongodb.client.MongoDatabase
import com.repository.Repositories
import com.repository.repositoriesInit
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(db: MongoDatabase) {
    val repositories: Repositories = repositoriesInit(db)
    routing {
        accountRoutes(repositories.accountRepository)
        friendsRoutes(repositories.accountRepository)
        get("/test") {
            call.respondText("Hello World")
        }
        authenticate {
            withAccessRight(0) {
                get("/") {
                    call.respondText("Ok")
                }
            }
        }
    }
}

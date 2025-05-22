package com

import com.database.configureDatabases
import com.routes.configureRouting
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

// TODO: Implement Ktor-Koin integration (Koin didn't support the Ktor version used when this app was developed)
fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            cause.printStackTrace()
            call.respondText( text = "Something went wrong! Please try again later.", status = HttpStatusCode.InternalServerError)
        }
    }
    val db = configureDatabases()
    configureSecurity()
    configureRouting(db)
}

package com.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.withAccessRight(minAccessRight: Int, build: Route.() -> Unit) {
    val route = createChild(object : RouteSelector() {
        override suspend fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
            return RouteSelectorEvaluation.Transparent
        }

    })
    route.install(RoleAuthorizationPlugin) {
        requiredAccessRight = minAccessRight
    }
    route.build()
}


class RoleBaseConfiguration {
    var requiredAccessRight: Int = 0
}

val RoleAuthorizationPlugin = createRouteScopedPlugin("RoleAuthorizationPlugin", ::RoleBaseConfiguration) {
    on(AuthenticationChecked) { call ->
        val principal = call.principal<JWTPrincipal>() ?: return@on
        val accessRight : Int = principal.payload.getClaim("accessRight").asInt() ?: 0
        val requiredAccessRight = pluginConfig.requiredAccessRight

        if (accessRight < requiredAccessRight) {
            call.respondText("You don’t have access to this resource.", status = HttpStatusCode.Unauthorized)
        }
    }
}
package com.utility

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.constants.TimeConstants
import io.ktor.server.application.*
import java.util.*



fun generateTokens(id: String, environment: ApplicationEnvironment) : HashMap<String, String> {

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.domain").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val accessToken = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("_id", id)
        .withClaim("accessRight", 1)
        .withExpiresAt(Date(System.currentTimeMillis() + TimeConstants.FIVE_MINUTES))
        .sign(Algorithm.HMAC256(secret))

    val refreshToken = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("_id", id)
        .withClaim("refresh_token", "CURRENT_REFRESH_TOKEN")
        .withExpiresAt(Date(System.currentTimeMillis() + TimeConstants.ONE_HUNDRED_DAYS))
        .sign(Algorithm.HMAC256(secret))

    return hashMapOf("accessToken" to accessToken, "refreshToken" to refreshToken)
}
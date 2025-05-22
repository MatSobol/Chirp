package com.example.chirp.data.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.example.chirp.constants.URL_REST
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.auth.providers.BearerTokens

class Client(private val authService: AuthService) {

    val value: HttpClient = HttpClient(Android) {
        defaultRequest { url(URL_REST) }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val tokens = authService.getTokens()
                    BearerTokens(tokens.accessToken, tokens.refreshToken)
                }
                refreshTokens {
                    val tokens = authService.refreshTokens()
                    BearerTokens(tokens.accessToken, tokens.refreshToken)
                }
            }
        }
    }
}
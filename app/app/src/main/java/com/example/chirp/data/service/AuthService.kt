package com.example.chirp.data.service

import android.content.Context
import android.content.SharedPreferences
import com.example.chirp.constants.URL_REST
import com.example.chirp.data.model.Tokens
import com.example.chirp.utility.getSharedPreferences
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class AuthService(private val sharedPrefs: SharedPreferences) {

    private val client: HttpClient = HttpClient(Android) {
        defaultRequest { url(URL_REST) }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    };

    suspend fun login(email: String, password: String): HttpResponse = withContext(Dispatchers.IO) {
        client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(hashMapOf("email" to email, "password" to password))
        }
    }

    suspend fun register(name: String, email: String, password: String): HttpResponse =
        withContext(Dispatchers.IO) {
            client.post("/register") {
                contentType(ContentType.Application.Json)
                setBody(hashMapOf("name" to name, "email" to email, "password" to password))
            }
        }

    suspend fun refreshTokens(): Tokens = withContext(Dispatchers.IO) {
        val tokens = getTokens()
        val response = client.post("/refresh-token") {
            header(HttpHeaders.Authorization, "Bearer ${tokens.refreshToken}")
        }
        val newTokens = response.body<Tokens>()
        saveTokens(newTokens)
        newTokens
    }

    fun getTokens(): Tokens {
        val accessToken = sharedPrefs.getString("ACCESS_TOKEN", null)
        val refreshToken = sharedPrefs.getString("REFRESH_TOKEN", null)
        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            throw IllegalStateException("No tokens")
        }
        return Tokens(accessToken, refreshToken)
    }

    fun logOut() {
        sharedPrefs.edit().clear().apply()
        println(sharedPrefs.getString("ACCOUNT", null))
    }

    fun isLoggedIn(): Boolean {
        val accessToken = sharedPrefs.getString("ACCESS_TOKEN", null)
        val refreshToken = sharedPrefs.getString("REFRESH_TOKEN", null)
        return !(accessToken.isNullOrBlank() || refreshToken.isNullOrBlank())
    }

    fun saveTokens(tokens: Tokens) {
        sharedPrefs.edit().putString("ACCESS_TOKEN", tokens.accessToken).apply()
        sharedPrefs.edit().putString("REFRESH_TOKEN", tokens.refreshToken).apply()
    }
}
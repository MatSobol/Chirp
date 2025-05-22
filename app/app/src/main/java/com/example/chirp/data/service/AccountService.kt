package com.example.chirp.data.service

import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountService(clientClass: Client) {
    private val client = clientClass.value
    suspend fun getClientInfo() = withContext(Dispatchers.IO) {
        client.get("/account")
    }
}
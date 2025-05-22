package com.example.chirp.data.service

import com.example.chirp.data.service.Client
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendsService(clientClass: Client) {
    private val client = clientClass.value
    suspend fun addFriend(id: String): HttpResponse = withContext(Dispatchers.IO) {
        client.post("/friends/$id")
    }

    suspend fun denyRequest(id: String): HttpResponse = withContext(Dispatchers.IO) {
        client.delete("friends/request/$id")
    }

    suspend fun sendRequest(email: String): HttpResponse = withContext(Dispatchers.IO) {
        client.post("friends/request/$email")
    }
}
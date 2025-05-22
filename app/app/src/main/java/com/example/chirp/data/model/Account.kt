package com.example.chirp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val email: String,
    val name: String,
    val accessRight: Int,
    val friends: ArrayList<Friend>,
    val friendRequests: ArrayList<Friend>
)
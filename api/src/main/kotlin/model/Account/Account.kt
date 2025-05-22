package com.model.Account

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val name: String,
    val email: String,
    val accessRight: Int,
    val friends: List<Friend>,
    val friendRequests: List<Friend>
)

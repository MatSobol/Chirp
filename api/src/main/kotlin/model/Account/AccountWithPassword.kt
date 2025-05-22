package com.model.Account

import kotlinx.serialization.Serializable

@Serializable
data class AccountWithPassword(
    val name: String,
    val email: String,
    val password: String,
    val accessRight: Int,
    val friends: List<Friend>,
    val friendRequests: List<Friend>
)

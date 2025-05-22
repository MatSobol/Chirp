package com.model.Account

import kotlinx.serialization.Serializable

@Serializable
data class AccountLogin(val email: String, val password: String)
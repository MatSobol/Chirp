package com.model.Account

import com.utility.isEmail
import kotlinx.serialization.Serializable

@Serializable
data class AccountRegister(val name: String, val email: String, val password: String) {
    init {
        require(name.length >= 5)
        require(password.length >= 5)
        require(isEmail(email))
    }
}

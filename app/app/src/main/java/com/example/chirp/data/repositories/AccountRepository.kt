package com.example.chirp.data.repositories

import android.content.SharedPreferences
import com.example.chirp.data.model.Account
import com.example.chirp.data.service.AccountService
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

class AccountRepository(
    private val accountService: AccountService,
    private val sharedPrefs: SharedPreferences
) {

    suspend fun initAccount(): Boolean {
        val response = accountService.getClientInfo()
        if (response.status == HttpStatusCode.OK) {
            val account = response.body<Account>()
            val string = Json.encodeToString(Account.serializer(), account)
            sharedPrefs.edit().putString("ACCOUNT", string).apply()
            return true
        }
        return false
    }

    fun getAccount(): Account {
        val string = sharedPrefs.getString("ACCOUNT", null)
        println(string)
        if (string === null) throw Exception("No account")
        return Json.decodeFromString(Account.serializer(), string)
    }

    fun modifyAccount(account: Account) {
        val string = Json.encodeToString(Account.serializer(), account)
        sharedPrefs.edit().putString("ACCOUNT", string).apply()
    }
}
package com.example.chirp.viewModel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.chirp.R
import com.example.chirp.data.model.Tokens
import com.example.chirp.data.repositories.AccountRepository
import com.example.chirp.data.service.AuthService
import com.example.chirp.utility.initAfterLogin
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform

class RegisterViewModel(private val authService: AuthService) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResultAuth = MutableLiveData<ResultAuth>()
    val registerResultAuth: LiveData<ResultAuth> = _registerResultAuth

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val response = authService.register(name, email, password)
            if (response.status == HttpStatusCode.OK) {
                authService.saveTokens(response.body<Tokens>())
                initAfterLogin()
                val accountRepository: AccountRepository = KoinPlatform.getKoin().get()
                if (!accountRepository.initAccount()) {
                    _registerResultAuth.value = ResultAuth(error = "Couldn't fetch account")
                    return@launch
                }
                _registerResultAuth.value =
                    ResultAuth(success = true)
            } else {
                val errorResponse = response.bodyAsText()
                _registerResultAuth.value = ResultAuth(error = errorResponse)
            }
        }
    }

    fun registerDataChanged(name: String, email: String, password: String, repeatPassword: String) {
        val nameError = if (!isNameValid(name)) R.string.invalid_name else null
        val emailError = if (!isEmailNameValid(email)) R.string.invalid_email else null
        val passwordError = if (!isPasswordValid(password)) R.string.invalid_password else null
        val repeatPasswordError = if (!isRepeatPasswordValid(
                password,
                repeatPassword
            )
        ) R.string.invalid_repeat_password else null
        _registerForm.value = RegisterFormState(
            nameError = nameError,
            emailError = emailError,
            passwordError = passwordError,
            repeatPasswordError = repeatPasswordError,
            isDataValid = emailError == null && passwordError == null
        )
    }

    private fun isNameValid(name: String): Boolean {
        return name.length > 5
    }

    private fun isEmailNameValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isRepeatPasswordValid(password: String, repeatPassword: String): Boolean {
        return password == repeatPassword
    }

}
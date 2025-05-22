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

class LoginViewModel(private val authService: AuthService) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResultAuth = MutableLiveData<ResultAuth>()
    val loginResultAuth: LiveData<ResultAuth> = _loginResultAuth

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = authService.login(email, password)
            if (response.status == HttpStatusCode.OK) {
                authService.saveTokens(response.body<Tokens>())
                initAfterLogin()
                val accountRepository: AccountRepository = KoinPlatform.getKoin().get()
                if (!accountRepository.initAccount()) {
                    _loginResultAuth.value = ResultAuth(error = "Couldn't fetch account")
                    return@launch
                }
                _loginResultAuth.value =
                    ResultAuth(success = true)
            } else {
                val errorResponse = response.bodyAsText()
                _loginResultAuth.value = ResultAuth(error = errorResponse)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        val emailError = if (!isEmailNameValid(username)) R.string.invalid_email else null
        val passwordError = if (!isPasswordValid(password)) R.string.invalid_password else null

        _loginForm.value = LoginFormState(
            emailError = emailError,
            passwordError = passwordError,
            isDataValid = emailError == null && passwordError == null
        )
    }

    private fun isEmailNameValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
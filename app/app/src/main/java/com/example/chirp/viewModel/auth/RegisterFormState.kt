package com.example.chirp.viewModel.auth

data class RegisterFormState (
    val nameError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val repeatPasswordError: Int? = null,
    val isDataValid: Boolean = false
)
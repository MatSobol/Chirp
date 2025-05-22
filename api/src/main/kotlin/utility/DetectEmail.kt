package com.utility

val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")

fun isEmail(email: String) : Boolean{
    return email.matches(EMAIL_REGEX)
}

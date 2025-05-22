package com.example.chirp.utility

import android.content.Context
import android.content.SharedPreferences

fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("chirp", Context.MODE_PRIVATE)
}
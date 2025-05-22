package com.example.chirp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class Chirp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}
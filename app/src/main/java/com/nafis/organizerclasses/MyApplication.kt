package com.nafis.organizerclasses

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Load the dark mode preference from SharedPreferences
        val preferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val isDarkMode = preferences.getBoolean("isDarkMode", false)

        // Apply the dark mode setting globally
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}

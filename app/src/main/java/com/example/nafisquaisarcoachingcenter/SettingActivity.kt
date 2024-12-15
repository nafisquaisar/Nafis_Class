package com.example.nafisquaisarcoachingcenter

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.nafisquaisarcoachingcenter.databinding.ActivitySettingBinding
import java.util.*

class SettingActivity : AppCompatActivity() {
   private lateinit var binding: ActivitySettingBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Initialize shared preferences
        preferences = getSharedPreferences("AppSettings", MODE_PRIVATE)

        // Set up Dark Mode Switch
        val isDarkMode = preferences.getBoolean("isDarkMode", false)
        binding.switchDarkMode.isChecked = isDarkMode
        setDarkMode(isDarkMode)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setDarkMode(isChecked)
            // Save the user's dark mode preference
            preferences.edit().putBoolean("isDarkMode", isChecked).apply()
        }



    }

    private fun setDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}

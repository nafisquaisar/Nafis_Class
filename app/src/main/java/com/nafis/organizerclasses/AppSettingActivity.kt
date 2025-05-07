package com.nafis.organizerclasses

import android.R
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.nafis.organizerclasses.databinding.ActivityAppSettingBinding

class AppSettingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAppSettingBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAppSettingBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.backarrowbtn.setOnClickListener { onBackPressed() }
        binding.titleName.text="Setting"

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

         val lang= arrayOf("English", "Hindi", "Urdu")
//        // Populate spinner with language options
//        val languages = listOf("English", "Hindi", "Urdu")
        val languageAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, // Default item layout
            lang
        )
        languageAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerLanguage.adapter = languageAdapter
//
//
//        // Load the saved language preference
//        val savedLanguage = preferences.getString("language", "en")
//        val savedPosition = when (savedLanguage) {
//            "hi" -> 1
//            "ur" -> 2
//            else -> 0
//        }
//        binding.spinnerLanguage.setSelection(savedPosition)
//
//        // Language selection listener
        binding.spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguageCode = when (position) {
                    1 -> "hi" // Hindi
                    2 -> "ur" // Urdu
                    else -> "en" // English
                }
//                changeLanguage(selectedLanguageCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.llTermsAndConditions.setOnClickListener {
            val intent = Intent(this@AppSettingActivity, TermsAndConditionsActivity::class.java)
            startActivity(intent)
        }


    }

//    private fun changeLanguage(languageCode: String) {
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//        val config = resources.configuration
//        config.setLocale(locale)
//        resources.updateConfiguration(config, resources.displayMetrics)
//
//        // Save the selected language preference
//        preferences.edit().putString("language", languageCode).apply()
//
//        // Refresh the UI manually without recreating the activity
//        val intent = intent
//        finish()
//        startActivity(intent)
//        overridePendingTransition(0, 0) // Optional: Prevent transition animation
//    }

    private fun setDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
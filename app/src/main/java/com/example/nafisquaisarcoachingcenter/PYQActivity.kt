package com.example.nafisquaisarcoachingcenter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.nafisquaisarcoachingcenter.databinding.ActivityPyqactivityBinding
import com.example.nafisquaisarcoachingcenter.fragment.BoardFragment

class PYQActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPyqactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPyqactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the initial title
        binding.titleName.text = "PYQ"

        // Add the BoardFragment on activity start and add it to the back stack
        supportFragmentManager.beginTransaction()
            .replace(R.id.pyqWrapper, BoardFragment())
            .addToBackStack("BoardFragment") // Add BoardFragment to the back stack
            .commit()

        // Handle back button click
        binding.backarrowbtn.setOnClickListener {
            onBackPressed() // Handle back navigation
        }
    }

    // Update the title dynamically
    fun updateTitle(title: String) {
        binding.titleName.text = title
        Log.d("PYQActivity", "Title updated to: $title")
    }

    // Handle back navigation to ensure proper fragment transitions
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            // Pop the top fragment from the stack, go back to the previous fragment
            supportFragmentManager.popBackStack()
        } else {
            // If no fragments left in the stack, finish the activity
            super.onBackPressed()
        }
    }
}

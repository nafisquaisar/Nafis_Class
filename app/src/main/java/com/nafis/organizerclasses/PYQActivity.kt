package com.nafis.organizerclasses

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.nafis.organizerclasses.databinding.ActivityPyqactivityBinding
import com.nafis.organizerclasses.fragment.BoardFragment

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

        binding.backarrowbtn.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()  // Pops the last fragment from the stack
            } else {
                finish()  // Closes the activity if no fragments in the back stack
            }
        }
    }

    // Update the title dynamically
    fun updateTitle(title: String) {
        binding.titleName.text = title
        Log.d("PYQActivity", "Title updated to: $title")
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            // Pop the top fragment from the stack
            supportFragmentManager.popBackStack()
        } else {
            // Call super to handle default back behavior (e.g., closing the activity)
            finish()
            super.onBackPressed()
        }
    }



}

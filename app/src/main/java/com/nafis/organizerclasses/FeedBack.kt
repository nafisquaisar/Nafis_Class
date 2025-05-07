package com.nafis.organizerclasses

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nafis.organizerclasses.databinding.ActivityFeedBackBinding

class FeedBack : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBackBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Back button functionality
        binding.backarrowbtn.setOnClickListener {
            onBackPressed()
        }

        // Send feedback button functionality
        binding.sendFeedbackButton.setOnClickListener {
            val userName = binding.feedbackName.text.toString().trim()
            val feedbackMessage = binding.feedbackMsg.text.toString().trim()

            // Validate inputs
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(feedbackMessage)) {
                Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create email intent
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // Only email apps should handle this
                putExtra(Intent.EXTRA_EMAIL, arrayOf("organizerfeedback@gmail.com")) // Recipient email
                putExtra(Intent.EXTRA_SUBJECT, "Feedback from $userName") // Subject line
                putExtra(
                    Intent.EXTRA_TEXT, """
                        Name: $userName
                        
                        Message:
                        $feedbackMessage
                    """.trimIndent() // Email body content
                )
            }

            // Try to send email
            try {
                startActivity(Intent.createChooser(emailIntent, "Send Feedback via"))
                binding.feedbackName.text=null
                binding.feedbackMsg.text=null
                Toast.makeText(this, "Feedback sent successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "No email app found to send feedback", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

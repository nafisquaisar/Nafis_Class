package com.nafis.organizerclasses.Internet

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.nafis.organizerclasses.FrontVIew
import com.nafis.organizerclasses.databinding.ActivityInternetNotAvailableBinding
import kotlinx.coroutines.*

class InternetNotAvailableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInternetNotAvailableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInternetNotAvailableBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.refreshbtn.setOnClickListener {
            rotateOnce(binding.refreshbtn)

            // Start coroutine for 3-second delay
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000) // 3-second delay
                startActivity(Intent(this@InternetNotAvailableActivity, FrontVIew::class.java))
                finish() // Optional: Finish the current activity
            }
        }
    }

    private fun rotateOnce(imageView: ImageView) {
        val animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f)
        animator.duration = 60000 // 60 seconds for one full rotation (slower)
        animator.interpolator = LinearInterpolator() // Smooth continuous rotation
        animator.repeatCount = ObjectAnimator.INFINITE // Infinite repetition for continuous rotation
        animator.start()
    }

}

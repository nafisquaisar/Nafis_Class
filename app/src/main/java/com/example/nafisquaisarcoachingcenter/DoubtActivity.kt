package com.example.nafisquaisarcoachingcenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.nafisquaisarcoachingcenter.databinding.ActivityDoubtBinding

class DoubtActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDoubtBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDoubtBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.backarrowbtn.setOnClickListener {
            onBackPressed()
        }
    }
}
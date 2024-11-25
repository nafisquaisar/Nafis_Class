package com.example.nafisquaisarcoachingcenter.coursecclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private  lateinit var binding :ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChatBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.backarrowbtn.setOnClickListener {
            onBackPressed()
        }
    }
}
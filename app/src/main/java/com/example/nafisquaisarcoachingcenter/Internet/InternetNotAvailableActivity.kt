package com.example.nafisquaisarcoachingcenter.Internet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.nafisquaisarcoachingcenter.FrontVIew
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.ActivityInternetNotAvailableBinding

class InternetNotAvailableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInternetNotAvailableBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityInternetNotAvailableBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
         binding.refreshbtn.setOnClickListener{
             startActivity(Intent(this,FrontVIew::class.java))
         }
    }
}
package com.example.nafisquaisarcoachingcenter.coursecclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.ActivityPdfviewerBinding

class PDFViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfviewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPdfviewerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

    }
}
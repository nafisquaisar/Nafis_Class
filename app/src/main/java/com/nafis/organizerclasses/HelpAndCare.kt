package com.nafis.organizerclasses

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.nafis.organizerclasses.databinding.ActivityHelpAndCareBinding

class HelpAndCare : AppCompatActivity() {
    private lateinit var binding: ActivityHelpAndCareBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHelpAndCareBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        button()
    }

    private fun button() {
        binding.emailValue.setOnClickListener {
            val emailIntent=Intent(Intent.ACTION_SENDTO).apply {
                data= Uri.parse("mailto:organizerclasses@gmail.com")
            }
            if(emailIntent.resolveActivity(packageManager)!=null){
                startActivity(emailIntent)
            }
        }

        binding.phoneValue.setOnClickListener {
            val phoneIntent=Intent(Intent.ACTION_DIAL).apply {
                data=Uri.parse("tel:9801999829")
            }
            startActivity(phoneIntent)
        }

        binding.websiteValue.setOnClickListener {
            val websiteIntent=Intent(Intent.ACTION_VIEW).apply {
                data= Uri.parse("https://organizerclasses.netlify.app/")
            }
            startActivity(websiteIntent)
        }

        binding.whatsappGroup.setOnClickListener {
            val groupLink="https://chat.whatsapp.com/FYundyZ3vEi6DD6852W2l9"
            openWhatsAppGroup(groupLink)
        }
        binding.whatsappChannel.setOnClickListener {
            val groupLink="https://whatsapp.com/channel/0029VasrZUh9MF92XVxWIo3v"
            openWhatsAppGroup(groupLink)
        }

        binding.telegramValue.setOnClickListener {
            val groupLink="https://t.me/+J4JCGQlDzuc1ZjJl"
            openTelegramGroup(groupLink)
        }


        binding.backarrowbtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun openWhatsAppGroup(groupLink: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(groupLink)
                setPackage("com.whatsapp") // Ensure the WhatsApp app is opened
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openTelegramGroup(groupLink: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(groupLink)
                setPackage("org.telegram.messenger") // Ensure the Telegram app is opened
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Telegram is not installed on your device.", Toast.LENGTH_SHORT).show()
        }
    }


}
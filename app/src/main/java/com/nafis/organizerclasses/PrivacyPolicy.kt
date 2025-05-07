package com.nafis.organizerclasses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import com.nafis.organizerclasses.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicy : AppCompatActivity() {
    private lateinit var binding:ActivityPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPrivacyPolicyBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.backarrowbtn.setOnClickListener { onBackPressed() }

        binding.privacyPolicy.text = Html.fromHtml(getString(R.string.Privacy_Policy), Html.FROM_HTML_MODE_LEGACY)
    }
}
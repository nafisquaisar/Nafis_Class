package com.nafis.organizerclasses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import com.nafis.organizerclasses.databinding.ActivityTermsAndConditionsBinding

class TermsAndConditionsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityTermsAndConditionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTermsAndConditionsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.backarrowbtn.setOnClickListener { onBackPressed() }

        binding.termAndCond.text = Html.fromHtml(getString(R.string.terms_conditions), Html.FROM_HTML_MODE_LEGACY)

    }
}
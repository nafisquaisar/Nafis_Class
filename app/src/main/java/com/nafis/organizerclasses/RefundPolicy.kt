package com.nafis.organizerclasses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import com.nafis.organizerclasses.databinding.ActivityRefundPolicyBinding

class RefundPolicy : AppCompatActivity() {
    private lateinit var binding:ActivityRefundPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRefundPolicyBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.backarrowbtn.setOnClickListener { onBackPressed() }

        binding.refundAndCancellationPolicy.text = Html.fromHtml(getString(R.string.Refund_Policy), Html.FROM_HTML_MODE_LEGACY)

    }
}
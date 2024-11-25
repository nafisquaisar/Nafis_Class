package com.example.nafisquaisarcoachingcenter.coursecclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.nafisquaisarcoachingcenter.PYQActivity
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.ActivityClassMainBinding
import com.example.nafisquaisarcoachingcenter.fragment.SubjectFragment

class ClassMainActivity() : AppCompatActivity() {
         private  lateinit var binding: ActivityClassMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityClassMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        // Retrieve the intent that started this activity
        val className = intent.getStringExtra("className")
        val BoardName = intent.getStringExtra("BoardName")
        val Test = intent.getStringExtra("test")

        binding.titleName.text=className!!
        binding.backarrowbtn.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()  // Pops the last fragment from the stack
            } else {
                finish()  // Closes the activity if no fragments in the back stack
            }
        }


        // Check if className is not null before proceeding
        supportFragmentManager.beginTransaction()
            .replace(R.id.wrapper, SubjectFragment(className,BoardName,Test))
            .commit()


    }


    // Function to customize the back button click
    fun setBackButtonClickListener(onClick: () -> Unit) {
        binding.backarrowbtn.setOnClickListener { onClick() }
    }

    fun updateTitle(title: String) {
        binding.titleName.text = title
        Log.d("title",title)
    }


}
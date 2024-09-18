package com.example.nafisquaisarcoachingcenter.coursecclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.ActivityClassMainBinding
import com.example.nafisquaisarcoachingcenter.fragment.SubjectFragment

class ClassMainActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityClassMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityClassMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        // Retrieve the intent that started this activity
        val className = intent.getStringExtra("className")
        val toolbar= binding.toolbarforActivity.toolbarForAll
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = className
        }

        // Check if className is not null before proceeding
        if (className != null) {
            // Replace the fragment with SubjectFragment and pass the className as argument
            supportFragmentManager.beginTransaction()
                .replace(R.id.wrapper, SubjectFragment(className))
                .commit()
        } else {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item.itemId

        if(id== android.R.id.home)
        {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
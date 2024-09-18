package com.example.nafisquaisarcoachingcenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nafisquaisarcoachingcenter.Object.testTitleObject
import com.example.nafisquaisarcoachingcenter.adapter.testCategoryAdapter
import com.example.nafisquaisarcoachingcenter.databinding.ActivityTestHomeBinding

class Test_home_activity : AppCompatActivity() {
    private lateinit var binding: ActivityTestHomeBinding
    private lateinit var toolbar: Toolbar
    private lateinit var rvAdapter: testCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTestHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar= binding.toolbarforActivity.toolbarForAll

        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "TEST SERIES"
        }

        binding.testRecylerView.layoutManager= LinearLayoutManager(this)
        rvAdapter= testCategoryAdapter(testTitleObject.getdata(),this)
        binding.testRecylerView.adapter=rvAdapter


    }
}
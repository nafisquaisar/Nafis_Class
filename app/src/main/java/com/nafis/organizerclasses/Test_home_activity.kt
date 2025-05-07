package com.nafis.organizerclasses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafis.organizerclasses.Object.testTitleObject
import com.nafis.organizerclasses.adapter.testCategoryAdapter
import com.nafis.organizerclasses.databinding.ActivityTestHomeBinding

class Test_home_activity : AppCompatActivity() {
    private lateinit var binding: ActivityTestHomeBinding
    private lateinit var rvAdapter: testCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTestHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.titleName.text="Test Series"
        binding.backarrowbtn.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()  // Pops the last fragment from the stack
            } else {
                finish()  // Closes the activity if no fragments in the back stack
            }
        }

        binding.testRecylerView.layoutManager= LinearLayoutManager(this)
        rvAdapter= testCategoryAdapter(testTitleObject.getdata(),this)
        binding.testRecylerView.adapter=rvAdapter


    }
}
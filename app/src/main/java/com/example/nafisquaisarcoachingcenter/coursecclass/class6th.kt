package com.example.nafisquaisarcoachingcenter.coursecclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nafisquaisarcoachingcenter.Object.allSubjectObject
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.adapter.subjectcategoryAdapter
import com.example.nafisquaisarcoachingcenter.databinding.ActivityClass5thBinding
import com.example.nafisquaisarcoachingcenter.databinding.ActivityClass6thBinding

class class6th : AppCompatActivity() {
    private lateinit var  binding: ActivityClass6thBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityClass6thBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val toolbar= binding.toolbarforActivity.toolbarForAll
//
//        setSupportActionBar(toolbar)



        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Class 6th"
        }


        // *************   Recycler View of all subject  ****************

//        binding.class6ththsubjectRecyler.layoutManager= GridLayoutManager(this,2)
//        var adapter= subjectcategoryAdapter(allSubjectObject.getSubjectData(), this)
//        binding.class6ththsubjectRecyler.adapter=adapter


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
package com.example.nafisquaisarcoachingcenter

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.nafisquaisarcoachingcenter.databinding.ActivityPyqactivityBinding
import com.example.nafisquaisarcoachingcenter.fragment.BoardFragment
import com.example.nafisquaisarcoachingcenter.fragment.SubjectFragment

class PYQActivity() : AppCompatActivity() {
    private lateinit var binding:ActivityPyqactivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPyqactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar= binding.toolbarforActivity.toolbarForAll
        setSupportActionBar(toolbar)



        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Previous Year Question"
        }

         supportFragmentManager.beginTransaction().replace(R.id.pyqWrapper,BoardFragment()).commit()

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
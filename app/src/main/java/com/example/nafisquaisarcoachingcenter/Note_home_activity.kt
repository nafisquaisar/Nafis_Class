package com.example.nafisquaisarcoachingcenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nafisquaisarcoachingcenter.Object.homeClassObject
import com.example.nafisquaisarcoachingcenter.adapter.categoryAdapter
import com.example.nafisquaisarcoachingcenter.databinding.ActivityNoteHomeBinding

class Note_home_activity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteHomeBinding
    private lateinit var toolbar: Toolbar
    private lateinit var adapter:categoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNoteHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar= binding.toolbarforActivity.toolbarForAll
        setSupportActionBar(toolbar)

        binding.NoteRecyclerView.layoutManager= GridLayoutManager(this,2)
        adapter= categoryAdapter(homeClassObject.getData(),this,"")
        binding.NoteRecyclerView.adapter=adapter


        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Note"
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
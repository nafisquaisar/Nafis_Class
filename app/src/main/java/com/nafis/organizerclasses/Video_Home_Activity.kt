package com.nafis.organizerclasses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.nafis.organizerclasses.Object.homeClassObject
import com.nafis.organizerclasses.adapter.categoryAdapter
import com.nafis.organizerclasses.databinding.ActivityVideoHomeBinding
import com.nafis.organizerclasses.model.categoryClass
import java.util.Locale

class Video_Home_Activity : AppCompatActivity() {
    private lateinit var binding:ActivityVideoHomeBinding
    private lateinit var toolbar: Toolbar
    private lateinit var adapter:categoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVideoHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        toolbar= binding.toolbarforActivity.toolbarForAll
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Video"
        }
        toolbar.navigationIcon?.setTint(resources.getColor(android.R.color.white))


        binding.CourseRecyclerView.layoutManager= GridLayoutManager(this,2)
        adapter= categoryAdapter(homeClassObject.getData(),this,"")
        binding.CourseRecyclerView.adapter=adapter


        binding.SearchCourse.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })

    }


    private fun filter(query: String?) {

        if(query!=null){
            val filteredList=ArrayList<categoryClass>()

            for (i in homeClassObject.getData()){
                if(i.catText.lowercase(Locale.ROOT).contains(query)){
                    filteredList.add(i)
                }
            }
            for (i in homeClassObject.getData()){
                if(i.catText.contains(query)){
                    filteredList.add(i)
                }
            }

            if(filteredList.isEmpty()){
                (Toast.makeText(this, "No data Found", Toast.LENGTH_SHORT)).show()
            }else{
                adapter.filterfun(filteredList)
            }
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
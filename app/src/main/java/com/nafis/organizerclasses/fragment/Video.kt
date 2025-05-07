package com.nafis.organizerclasses.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.nafis.organizerclasses.Object.homeClassObject
import com.nafis.organizerclasses.adapter.categoryAdapter
import com.nafis.organizerclasses.databinding.FragmentVideoBinding
import com.nafis.organizerclasses.model.categoryClass
import java.util.Locale


class Video : Fragment() {
  private lateinit var binding :FragmentVideoBinding
      private lateinit var adapter:categoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentVideoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.CourseRecyclerView.layoutManager= GridLayoutManager(requireContext(),2)
        adapter= categoryAdapter(homeClassObject.getData(),requireActivity())
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

            if(filteredList.isEmpty()){
                (Toast.makeText(context, "No data Found", Toast.LENGTH_SHORT)).show()
            }else{
                adapter.filterfun(filteredList)
            }
        }


    }
}
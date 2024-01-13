package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nafisquaisarcoachingcenter.Object.homeClassObject
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.adapter.categoryAdapter
import com.example.nafisquaisarcoachingcenter.databinding.FragmentHomeBinding
import com.example.nafisquaisarcoachingcenter.model.categoryClass
import com.google.android.gms.common.internal.Constants


class Home : Fragment() {
      private val binding:FragmentHomeBinding by lazy {
          FragmentHomeBinding.inflate(layoutInflater)
      }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

    companion object {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       binding.CourseRecyclerView.layoutManager=GridLayoutManager(requireContext(),2)
        var adapter=categoryAdapter(homeClassObject.getData(),requireActivity())
        binding.CourseRecyclerView.adapter=adapter
        binding.CourseRecyclerView.setHasFixedSize(true)

    }



}
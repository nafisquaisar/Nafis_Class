package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nafisquaisarcoachingcenter.Object.allSubjectObject
import com.example.nafisquaisarcoachingcenter.adapter.subjectcategoryAdapter
import com.example.nafisquaisarcoachingcenter.databinding.FragmentSubjectBinding


class SubjectFragment(var className: String) : Fragment() {
    private lateinit var binding: FragmentSubjectBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSubjectBinding.inflate(inflater,container,false)

//         *************   Recycler View of all subject  ****************
        binding.class8ththsubjectRecyler.layoutManager= GridLayoutManager(requireContext(),2)
        var adapter= subjectcategoryAdapter(allSubjectObject.getSubjectData(className), requireActivity())
        binding.class8ththsubjectRecyler.adapter=adapter


        return binding.root


    }
}
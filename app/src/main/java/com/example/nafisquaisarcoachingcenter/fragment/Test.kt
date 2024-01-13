package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nafisquaisarcoachingcenter.Object.testTitleObject
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.adapter.testCategoryAdapter
import com.example.nafisquaisarcoachingcenter.databinding.FragmentTestBinding


class Test : Fragment() {
    private lateinit var binding: FragmentTestBinding
    private lateinit var rvAdapter: testCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentTestBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.testRecylerView.layoutManager=LinearLayoutManager(context)
        rvAdapter=testCategoryAdapter(testTitleObject.getdata(),requireActivity())
        binding.testRecylerView.adapter=rvAdapter

    }
}
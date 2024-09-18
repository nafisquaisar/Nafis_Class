package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.FragmentDppVIewBinding


class DppFragmentVIew : Fragment() {
  private lateinit var binding: FragmentDppVIewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDppVIewBinding.inflate(inflater,container,false)
        return binding.root
    }


}
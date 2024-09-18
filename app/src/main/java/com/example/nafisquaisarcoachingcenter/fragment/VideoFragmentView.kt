package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.nafisquaisarcoachingcenter.databinding.FragmentVideoBinding
import com.example.nafisquaisarcoachingcenter.databinding.FragmentVideoViewBinding


class VideoFragmentView : Fragment() {
    private lateinit var binding: FragmentVideoViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentVideoViewBinding.inflate(inflater,container,false)
        return binding.root
    }


}
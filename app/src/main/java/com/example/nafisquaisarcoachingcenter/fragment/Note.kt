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
import com.example.nafisquaisarcoachingcenter.databinding.FragmentNoteBinding

class Note : Fragment() {
     private  lateinit var binding: FragmentNoteBinding
    private lateinit var adapter:categoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentNoteBinding.inflate(inflater,container,false)


        binding.NoteRecyclerView.layoutManager= GridLayoutManager(requireContext(),2)
        adapter= categoryAdapter(homeClassObject.getData(),requireActivity())
        binding.NoteRecyclerView.adapter=adapter
        return binding.root
    }

}
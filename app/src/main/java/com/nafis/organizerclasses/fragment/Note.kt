package com.nafis.organizerclasses.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.nafis.organizerclasses.Object.homeClassObject
import com.nafis.organizerclasses.PYQActivity
import com.nafis.organizerclasses.adapter.categoryAdapter
import com.nafis.organizerclasses.databinding.FragmentNoteBinding

class Note(var BoardName: String = "") : Fragment() {
    private lateinit var binding: FragmentNoteBinding
    private lateinit var adapter: categoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(inflater, container, false)

        // Safely cast the activity to PYQActivity
        if (BoardName.isNotEmpty() && activity is PYQActivity) {
            (activity as PYQActivity).updateTitle(BoardName)
        }

        binding.NoteRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = categoryAdapter(homeClassObject.getData(), requireActivity(), BoardName)
        binding.NoteRecyclerView.adapter = adapter
        return binding.root
    }
}

package com.nafis.organizerclasses.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.nafis.organizerclasses.DIffUtilCallBack.BoardItemCallback
import com.nafis.organizerclasses.Object.BoardObject
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.adapter.BoardAdapter
import com.nafis.organizerclasses.databinding.FragmentBoardBinding
import com.nafis.organizerclasses.model.categoryClass

class BoardFragment() : Fragment() {
  private lateinit var binding: FragmentBoardBinding
    private lateinit var adapter:BoardAdapter

    private val boardCallback by lazy {
        object :BoardItemCallback{
            override fun onBoardClick(item: categoryClass, position: Int) {
                var fm=(requireContext() as AppCompatActivity).supportFragmentManager
                fm.beginTransaction().replace(R.id.pyqWrapper,Note(item.catText)).commit()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentBoardBinding.inflate(inflater,container,false)
        binding.BoardRecyclerView.layoutManager= GridLayoutManager(requireContext(),2)
        adapter= BoardAdapter(callback = boardCallback)
        binding.BoardRecyclerView.adapter=adapter
        adapter.submitList(BoardObject.getData())
        return binding.root
    }

}
package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.BoardItemCallback
import com.example.nafisquaisarcoachingcenter.Note_home_activity
import com.example.nafisquaisarcoachingcenter.Object.BoardObject
import com.example.nafisquaisarcoachingcenter.Object.homeClassObject
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.adapter.BoardAdapter
import com.example.nafisquaisarcoachingcenter.adapter.categoryAdapter
import com.example.nafisquaisarcoachingcenter.databinding.FragmentBoardBinding
import com.example.nafisquaisarcoachingcenter.model.categoryClass

class BoardFragment() : Fragment() {
  private lateinit var binding: FragmentBoardBinding
    private lateinit var adapter:BoardAdapter

    private val boardCallback by lazy {
        object :BoardItemCallback{
            override fun onBoardClick(item: categoryClass, position: Int) {
                Toast.makeText(requireContext(),"Click ${item.catText}",Toast.LENGTH_SHORT).show()
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
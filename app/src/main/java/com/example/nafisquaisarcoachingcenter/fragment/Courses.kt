package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalChatCallback
import com.example.nafisquaisarcoachingcenter.Object.TotalChat
import com.example.nafisquaisarcoachingcenter.adapter.TotalChatAdapter
import com.example.nafisquaisarcoachingcenter.databinding.FragmentCoursesBinding


class Courses : Fragment() {
  private  lateinit var binding: FragmentCoursesBinding
  private lateinit var list:ArrayList<TotalChat>
  private lateinit var adapter: TotalChatAdapter

  private val callback by lazy{
      object :TotalChatCallback{
          override fun onTotalChatClick(item: TotalChat, position: Int) {
              Toast.makeText(requireContext(),item.title,Toast.LENGTH_SHORT).show()
          }

      }
  }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentCoursesBinding.inflate(layoutInflater,container,false)
        fetchData()
        return binding.root
    }

    private fun fetchData() {

    }


}
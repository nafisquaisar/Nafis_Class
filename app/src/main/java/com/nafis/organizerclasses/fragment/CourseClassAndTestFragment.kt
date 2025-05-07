package com.nafis.nafis.nf2024.organizeradminpanel.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nafis.organizerclasses.adapter.CourseClassTestPagerAdapter
import com.nafis.organizerclasses.databinding.FragmentCourseClassAndTestBinding
import com.google.android.material.tabs.TabLayoutMediator

class CourseClassAndTestFragment(var courseId: String, var courseName: String?) : Fragment() {
    private lateinit var binding: FragmentCourseClassAndTestBinding
    private lateinit var adapter: CourseClassTestPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentCourseClassAndTestBinding.inflate(inflater,container,false)


        adapter = CourseClassTestPagerAdapter(childFragmentManager, lifecycle,courseId= courseId,courseName=courseName)
        binding.lectureContainer.adapter = adapter



        TabLayoutMediator(binding.tablayout, binding.lectureContainer) { tab, position ->
            tab.text = if (position == 0) "All Class" else "Test"
        }.attach()

        // Set default tab based on courseName
        if (courseName.isNullOrEmpty()) {
            binding.lectureContainer.setCurrentItem(1, false) // Select position 1 ("Test")
        }

        return binding.root
    }

}
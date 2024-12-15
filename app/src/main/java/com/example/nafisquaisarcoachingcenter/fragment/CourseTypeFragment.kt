package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.adapter.CourseViewPagerAdapter
import com.example.nafisquaisarcoachingcenter.databinding.FragmentCourseTypeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CourseTypeFragment : Fragment() {
    private lateinit var binding: FragmentCourseTypeBinding
    private lateinit var adapter: CourseViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseTypeBinding.inflate(inflater, container, false)

        // Initialize the ViewPager2 adapter
        adapter = CourseViewPagerAdapter(childFragmentManager, lifecycle)
        binding.lectureContainer.adapter = adapter

        // Link TabLayout and ViewPager2 using TabLayoutMediator
        TabLayoutMediator(binding.tablayout, binding.lectureContainer) { tab, position ->
            tab.text = if (position == 0) "Courses" else "My Courses"
        }.attach()

        return binding.root
    }
}

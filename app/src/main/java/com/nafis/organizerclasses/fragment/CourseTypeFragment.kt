package com.nafis.organizerclasses.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nafis.organizerclasses.adapter.CourseViewPagerAdapter
import com.nafis.organizerclasses.databinding.FragmentCourseTypeBinding
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

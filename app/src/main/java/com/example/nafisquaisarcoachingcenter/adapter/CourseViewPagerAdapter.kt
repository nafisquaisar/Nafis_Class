package com.example.nafisquaisarcoachingcenter.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nafisquaisarcoachingcenter.fragment.Courses
import com.example.nafisquaisarcoachingcenter.fragment.MyCourseFragment

class CourseViewPagerAdapter( fragmentmanager: FragmentManager, lifecycle: Lifecycle
     ):FragmentStateAdapter(fragmentmanager,lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
          return  if(position==0){
               Courses()
           }else{
               MyCourseFragment()
           }
    }
}
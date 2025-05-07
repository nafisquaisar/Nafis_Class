package com.nafis.organizerclasses.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nafis.organizerclasses.fragment.CourseTestFragment
import com.nafis.organizerclasses.fragment.SubjectFragment


class CourseClassTestPagerAdapter(
    fragmentmanager: FragmentManager, lifecycle: Lifecycle, var courseId: String?, var courseName: String?
): FragmentStateAdapter(fragmentmanager,lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
          return  if(position==0){
                    SubjectFragment(courseId=courseId!!, courseName = courseName)
           }else{
                  CourseTestFragment(courseId = courseId,courseName=courseName)
           }
    }
}
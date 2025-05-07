package com.nafis.organizerclasses.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.nafis.organizerclasses.adapter.ViewPagerAdapter
import com.nafis.organizerclasses.activity.ClassMainActivity
import com.nafis.organizerclasses.databinding.FragmentLectureBinding
import com.google.android.material.tabs.TabLayout


class LectureFragment(
    var classname: String?="",
    var subname: String?="",
    var chapname: String?="",
    var courseId: String?=null,
    var subjectId: String?=null,
    var chapterId: String?=null,
    var courseChapterName: String?=null
) : Fragment() {
   private lateinit var binding: FragmentLectureBinding
   private lateinit var viewPager2: ViewPager2
   private lateinit var tabLayout: TabLayout
   private lateinit var adapter:ViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentLectureBinding.inflate(inflater,container,false)
        // =========Initialize ViewPager2 and TabLayout=========
        viewPager2 = binding.lectureContainer
        tabLayout = binding.tablayout

        // ============set adapter for viewpager2============
        val fragmentManager = childFragmentManager


        if(courseId!=null && subjectId!=null && chapterId!=null && courseChapterName!=null){
            adapter = ViewPagerAdapter(fragmentmanager = fragmentManager, lifecycle=lifecycle,courseId=courseId, subjectId = subjectId, chapterId = chapterId, courseChapterName = courseChapterName)
            (activity as ClassMainActivity).updateTitle(courseChapterName.toString())

        }else{
            adapter = ViewPagerAdapter(fragmentmanager =  fragmentManager,lifecycle= lifecycle, classname = classname, subName = subname, chapterName  = chapname)
            (activity as ClassMainActivity).updateTitle(chapname.toString())
        }
        viewPager2.adapter = adapter



        //=========set tab=====================
        tabLayout.addTab(tabLayout.newTab().setText("Video"))
        tabLayout.addTab(tabLayout.newTab().setText("Note"))
        tabLayout.addTab(tabLayout.newTab().setText("DPP"))
        viewPager2.adapter=adapter

        //============== set view pager with tab============
        tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab!=null){
                    viewPager2.currentItem=tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        return binding.root
    }


}
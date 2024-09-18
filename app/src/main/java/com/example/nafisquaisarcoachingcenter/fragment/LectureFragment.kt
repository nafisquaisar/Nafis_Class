package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.nafisquaisarcoachingcenter.adapter.ViewPagerAdapter
import com.example.nafisquaisarcoachingcenter.databinding.FragmentLectureBinding
import com.google.android.material.tabs.TabLayout


class LectureFragment(var classname: String?, var subname: String?, var chapname: String?) : Fragment() {
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
        adapter = ViewPagerAdapter(fragmentManager, lifecycle,classname,subname,chapname)
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
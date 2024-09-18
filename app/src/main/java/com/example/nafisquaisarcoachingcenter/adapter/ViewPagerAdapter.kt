package com.example.nafisquaisarcoachingcenter.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nafisquaisarcoachingcenter.fragment.DppFragmentVIew
import com.example.nafisquaisarcoachingcenter.fragment.NoteFragmentView
import com.example.nafisquaisarcoachingcenter.fragment.VideoFragmentView

class ViewPagerAdapter(fragmentmanager:FragmentManager,lifecycle:Lifecycle,
                       var classname:String?,var subName:String?,var chapterName:String?

):FragmentStateAdapter(fragmentmanager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return if(position==0){
            VideoFragmentView()
        }else if(position==1){
            NoteFragmentView(classname,subName,chapterName)
        }else{
            DppFragmentVIew()
        }
    }
}
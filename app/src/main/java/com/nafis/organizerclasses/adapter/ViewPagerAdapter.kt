package com.nafis.organizerclasses.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nafis.organizerclasses.fragment.DppFragmentVIew
import com.nafis.organizerclasses.fragment.NoteFragmentView
import com.nafis.organizerclasses.fragment.VideoFragmentView

class ViewPagerAdapter(fragmentmanager:FragmentManager,lifecycle:Lifecycle,
                       var classname:String?="",var subName:String?="",var chapterName:String?="",
                       var courseId: String?=null,
                       var subjectId: String?=null,
                       var chapterId: String?=null,
                       var courseChapterName: String?=null

):FragmentStateAdapter(fragmentmanager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       if(courseId!=null && subjectId!=null && chapterId!=null && courseChapterName!=null){
           return if(position==0){
               VideoFragmentView(courseId=courseId, subjectId = subjectId, chapterId = chapterId, courseChapterName = courseChapterName)
           }else if(position==1){
               NoteFragmentView(courseId=courseId, subjectId = subjectId, chapterId = chapterId, courseChapterName = courseChapterName   )
           }else{
               DppFragmentVIew(courseId=courseId, subjectId = subjectId, chapterId = chapterId, courseChapterName = courseChapterName)
           }
       }else{
           return if(position==0){
               VideoFragmentView(classname,subName,chapterName)
           }else if(position==1){
               NoteFragmentView(classname,subName,chapterName)
           }else{
               DppFragmentVIew(classname,subName,chapterName)
           }
       }
    }
}
package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.nafis.nf2024.organizeradminpanel.DiffutilCallBack.ChapterCallback
import com.example.nafis.nf2024.organizeradminpanel.DiffutilCallBack.DiffChapterCallBack
import com.example.nafisquaisarcoachingcenter.databinding.HomeItemBinding
import com.example.nafisquaisarcoachingcenter.model.Chapter


class CourseChapterAdapter(var context: Context, var callback: ChapterCallback, val courseList:ArrayList<Chapter>):
    ListAdapter<Chapter, CourseChapterViewHolder>(DiffChapterCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseChapterViewHolder {
          val binding=HomeItemBinding.inflate(LayoutInflater.from(context),parent,false)
          return CourseChapterViewHolder(binding,context,callback)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: CourseChapterViewHolder, position: Int) {
           var item=courseList[position]
           holder.bind(item)
    }
}
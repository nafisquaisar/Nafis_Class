package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.CourseVideoCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DiffCourseVideoCallBack
import com.example.nafisquaisarcoachingcenter.databinding.VideoItemBinding
import com.example.nafisquaisarcoachingcenter.model.Video

class CourseVideoAdapter(var context: Context, var callback: CourseVideoCallback, val courseList:ArrayList<Video>):
    ListAdapter<Video, CourseVideoViewHolder>(DiffCourseVideoCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseVideoViewHolder {
          val binding= VideoItemBinding.inflate(LayoutInflater.from(context),parent,false)
          return CourseVideoViewHolder(binding,context,callback)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: CourseVideoViewHolder, position: Int) {
           var item=courseList[position]
           holder.bind(item)
    }
}
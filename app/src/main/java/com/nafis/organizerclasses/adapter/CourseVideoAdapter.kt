package com.nafis.organizerclasses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nafis.organizerclasses.DIffUtilCallBack.CourseVideoCallback
import com.nafis.organizerclasses.DIffUtilCallBack.DiffCourseVideoCallBack
import com.nafis.organizerclasses.databinding.VideoItemBinding
import com.nafis.organizerclasses.model.Video

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
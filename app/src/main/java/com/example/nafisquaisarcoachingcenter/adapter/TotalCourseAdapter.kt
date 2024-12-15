package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DiffTotalCourseCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalCourseCallback
import com.example.nafisquaisarcoachingcenter.databinding.CourseItemBinding
import com.example.nafisquaisarcoachingcenter.model.TotalCourse


class TotalCourseAdapter(var context: Context, var callback: TotalCourseCallback, var list: List<TotalCourse>): ListAdapter<TotalCourse, TotalCourseViewHolder>(DiffTotalCourseCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TotalCourseViewHolder {
        var binding=CourseItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return TotalCourseViewHolder(binding,callback,context)
    }

    override fun onBindViewHolder(holder: TotalCourseViewHolder, position: Int) {
         var model=list[position]
        holder.bind(model)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
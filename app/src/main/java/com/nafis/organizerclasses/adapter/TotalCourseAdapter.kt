package com.nafis.organizerclasses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nafis.organizerclasses.DIffUtilCallBack.DiffTotalCourseCallback
import com.nafis.organizerclasses.DIffUtilCallBack.TotalCourseCallback
import com.nafis.organizerclasses.databinding.CourseItemBinding
import com.nafis.organizerclasses.model.TotalCourse


class TotalCourseAdapter(var context: Context, var callback: TotalCourseCallback, var list: List<TotalCourse>): ListAdapter<TotalCourse, TotalCourseViewHolder>(DiffTotalCourseCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TotalCourseViewHolder {
        var binding=CourseItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return TotalCourseViewHolder(binding,callback,context)
    }

    override fun onBindViewHolder(holder: TotalCourseViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
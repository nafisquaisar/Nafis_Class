package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.CourseNoteCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DiffCourseNoteCallBack
import com.example.nafisquaisarcoachingcenter.databinding.NoteItemBinding
import com.example.nafisquaisarcoachingcenter.model.Note


class CourseNoteAdapter(var context: Context, var callback: CourseNoteCallback, val courseList:ArrayList<Note>):
    ListAdapter<Note, CourseNoteViewHolder>(DiffCourseNoteCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseNoteViewHolder {
          val binding= NoteItemBinding.inflate(LayoutInflater.from(context),parent,false)
          return CourseNoteViewHolder(binding,context,callback)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: CourseNoteViewHolder, position: Int) {
           var item=courseList[position]
           holder.bind(item)
    }
}
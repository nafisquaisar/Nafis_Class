package com.nafis.organizerclasses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nafis.organizerclasses.DIffUtilCallBack.CourseNoteCallback
import com.nafis.organizerclasses.DIffUtilCallBack.DiffCourseNoteCallBack
import com.nafis.organizerclasses.databinding.NoteItemBinding
import com.nafis.organizerclasses.model.Note


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
package com.nafis.organizerclasses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nafis.nafis.nf2024.organizeradminpanel.DiffutilCallBack.DiffSubjectCallBack
import com.nafis.nafis.nf2024.organizeradminpanel.DiffutilCallBack.SubjectCallback
import com.nafis.organizerclasses.databinding.AllSubjectLayoutBinding
import com.nafis.organizerclasses.model.Subject

class CourseSubjectAdapter(var context: Context, var callback: SubjectCallback, val courseList:ArrayList<Subject>):
    ListAdapter<Subject, CourseSubjectViewHolder>(DiffSubjectCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseSubjectViewHolder {
          val binding=AllSubjectLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
          return CourseSubjectViewHolder(binding,context,callback)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: CourseSubjectViewHolder, position: Int) {
           var item=courseList[position]
           holder.bind(item)
    }
}
package com.nafis.organizerclasses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nafis.organizerclasses.DIffUtilCallBack.BoardItemCallback
import com.nafis.organizerclasses.DIffUtilCallBack.DiffBoardCallback
import com.nafis.organizerclasses.databinding.CourseBinding
import com.nafis.organizerclasses.model.categoryClass

class BoardAdapter(var callback: BoardItemCallback): ListAdapter<categoryClass, BoardViewHolder>(
    DiffBoardCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
    var binding= CourseBinding.inflate(LayoutInflater.from(parent.context),parent,false)
     return BoardViewHolder(binding,callback)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
            val currItem=getItem(position)
            holder.bind(currItem)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
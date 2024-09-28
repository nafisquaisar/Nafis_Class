package com.example.nafisquaisarcoachingcenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.BoardItemCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DiffBoardCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DiffNoteCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DiffVideoCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.NoteItemCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.VideoItemCallback
import com.example.nafisquaisarcoachingcenter.databinding.CourseBinding
import com.example.nafisquaisarcoachingcenter.databinding.VideoItemBinding
import com.example.nafisquaisarcoachingcenter.model.categoryClass

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
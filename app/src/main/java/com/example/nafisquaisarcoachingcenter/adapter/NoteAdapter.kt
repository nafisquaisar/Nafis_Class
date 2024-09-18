package com.example.nafisquaisarcoachingcenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DiffNoteCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.NoteItemCallback
import com.example.nafisquaisarcoachingcenter.databinding.NoteItemBinding
import com.example.nafisquaisarcoachingcenter.model.NoteModel

class NoteAdapter(var callback:NoteItemCallback): ListAdapter<NoteModel, NoteViewHolder>(DiffNoteCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
    var binding=NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
     return NoteViewHolder(binding,callback)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
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
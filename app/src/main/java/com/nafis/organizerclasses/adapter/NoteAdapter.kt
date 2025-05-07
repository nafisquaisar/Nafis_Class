package com.nafis.organizerclasses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nafis.organizerclasses.DIffUtilCallBack.DiffNoteCallback
import com.nafis.organizerclasses.DIffUtilCallBack.NoteItemCallback
import com.nafis.organizerclasses.databinding.NoteItemBinding
import com.nafis.organizerclasses.databinding.PyqItemBinding
import com.nafis.organizerclasses.model.NoteModel

class NoteAdapter(var callback:NoteItemCallback,var pyq:String=" "): ListAdapter<NoteModel, NoteViewHolder >(DiffNoteCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = when(pyq) {
            "PYQ Adapter" -> {
                // Inflate the layout for "PYQ Adapter"
                PyqItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }
            else -> {
                // Inflate the default layout
                NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }
        }
     return NoteViewHolder(binding,callback )
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
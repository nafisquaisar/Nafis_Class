package com.example.nafisquaisarcoachingcenter.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.NoteItemCallback
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.NoteItemBinding
import com.example.nafisquaisarcoachingcenter.model.NoteModel

class NoteViewHolder(val binding: NoteItemBinding,val callback: NoteItemCallback):RecyclerView.ViewHolder(binding.root){

    fun bind(item:NoteModel){
         binding.apply {
             ChapterName.text=item.title
             date.text=item.date.toString()
             pdfIcon.setImageResource(R.drawable.pdf_icon)
             itemView.setOnClickListener {
                   callback.onNoteClick(item, position = position)
             }
         }
    }

}

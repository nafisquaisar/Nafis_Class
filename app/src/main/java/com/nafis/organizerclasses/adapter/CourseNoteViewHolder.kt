package com.nafis.organizerclasses.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.nafis.organizerclasses.DIffUtilCallBack.CourseNoteCallback
import com.nafis.organizerclasses.databinding.NoteItemBinding
import com.nafis.organizerclasses.model.Note


class CourseNoteViewHolder(
    var binding: NoteItemBinding, var context: Context, var callback: CourseNoteCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Note) {
        binding.apply {
            ChapterName.text = item.noteTitle
            date.text = item.date
        }

        itemView.setOnClickListener {
            callback.onNoteClick(item)
        }
    }

}

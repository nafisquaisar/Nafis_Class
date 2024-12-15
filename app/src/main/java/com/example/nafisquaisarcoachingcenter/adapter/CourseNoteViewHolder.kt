package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.CourseNoteCallback
import com.example.nafisquaisarcoachingcenter.databinding.NoteItemBinding
import com.example.nafisquaisarcoachingcenter.model.Note


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

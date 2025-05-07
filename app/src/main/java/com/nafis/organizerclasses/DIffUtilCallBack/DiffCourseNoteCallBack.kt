package com.nafis.organizerclasses.DIffUtilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.nafis.organizerclasses.model.Note


class DiffCourseNoteCallBack: DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
           return oldItem.noteId==newItem.noteId
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
          return oldItem.noteTitle==newItem.noteTitle &&
                  oldItem.noteUrl==newItem.noteUrl &&
                  oldItem.date==newItem.date

                     
    }
}
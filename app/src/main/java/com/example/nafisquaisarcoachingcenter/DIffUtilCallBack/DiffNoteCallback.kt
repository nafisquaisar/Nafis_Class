package com.example.nafisquaisarcoachingcenter.DIffUtilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.example.nafisquaisarcoachingcenter.model.NoteModel

class DiffNoteCallback :DiffUtil.ItemCallback<NoteModel>() {
    override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
               return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
              return oldItem.title==newItem.title &&
                           oldItem.clasname==newItem.clasname &&
                           oldItem.subname==newItem.subname &&
                           oldItem.chapname==newItem.chapname &&
                           oldItem.date==newItem.date &&
                           oldItem.pdfUrl==newItem.pdfUrl
    }
}
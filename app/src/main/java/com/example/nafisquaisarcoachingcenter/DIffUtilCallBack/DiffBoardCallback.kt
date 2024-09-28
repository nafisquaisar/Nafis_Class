package com.example.nafisquaisarcoachingcenter.DIffUtilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.example.nafisquaisarcoachingcenter.model.NoteModel
import com.example.nafisquaisarcoachingcenter.model.categoryClass

class DiffBoardCallback :DiffUtil.ItemCallback<categoryClass>() {
    override fun areItemsTheSame(oldItem: categoryClass, newItem: categoryClass): Boolean {
               return oldItem.catText==newItem.catText
    }

    override fun areContentsTheSame(oldItem: categoryClass, newItem: categoryClass): Boolean {
              return oldItem.catImage==newItem.catImage
    }
}
package com.nafis.organizerclasses.DIffUtilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.nafis.organizerclasses.model.categoryClass

class DiffBoardCallback :DiffUtil.ItemCallback<categoryClass>() {
    override fun areItemsTheSame(oldItem: categoryClass, newItem: categoryClass): Boolean {
               return oldItem.catText==newItem.catText
    }

    override fun areContentsTheSame(oldItem: categoryClass, newItem: categoryClass): Boolean {
              return oldItem.catImage==newItem.catImage
    }
}
package com.nafis.nafis.nf2024.organizeradminpanel.DiffutilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.nafis.organizerclasses.model.Chapter

class DiffChapterCallBack: DiffUtil.ItemCallback<Chapter>() {
    override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
           return oldItem.chapId==newItem.chapId
    }

    override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
          return oldItem.chapName==newItem.chapName
    }
}
package com.example.nafis.nf2024.organizeradminpanel.DiffutilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.example.nafisquaisarcoachingcenter.model.Subject

class DiffSubjectCallBack: DiffUtil.ItemCallback<Subject>() {
    override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
           return oldItem.subjectId==newItem.subjectId
    }

    override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
          return oldItem.subjectName==newItem.subjectName
    }
}
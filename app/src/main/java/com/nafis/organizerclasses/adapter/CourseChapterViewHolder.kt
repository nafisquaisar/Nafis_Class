package com.nafis.organizerclasses.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nafis.nafis.nf2024.organizeradminpanel.DiffutilCallBack.ChapterCallback
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.databinding.HomeItemBinding
import com.nafis.organizerclasses.model.Chapter

class CourseChapterViewHolder(
    var binding:HomeItemBinding, var context: Context
    , var callback: ChapterCallback
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Chapter){
        binding.apply {
            miansubname.text=item.chapName
            when(item.chapName){
                "Science"->{
                    subjectChapterIcon.setImageResource(R.drawable.sciencelogo)
                }
                "Chemistry"->{
                    subjectChapterIcon.setImageResource(R.drawable.chemistrysubjectlogo)
                }
                else->{
                    subjectChapterIcon.setImageResource(R.drawable.course)
                }
            }
            maindesp.visibility=View.GONE
        }


        itemView.setOnClickListener {
            callback.onChapterClick(item)
        }


    }


}

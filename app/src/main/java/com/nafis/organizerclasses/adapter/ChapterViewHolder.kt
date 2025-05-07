package com.nafis.organizerclasses.adapter

import androidx.recyclerview.widget.RecyclerView
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.databinding.HomeItemBinding
import com.nafis.organizerclasses.model.ChapterModel

class ChapterViewHolder(private val binding: HomeItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChapterModel){
        binding.miansubname.text=item.chapname
        binding.maindesp.text=item.des
        if(item.subname=="Physics"){
            binding.subjectChapterIcon.setImageResource(R.drawable.physic_chapter_icon2)
        }else if(item.subname=="Chemistry"){
            binding.subjectChapterIcon.setImageResource(R.drawable.chem_chapter_icon)
        }else if(item.subname=="Math"){
            binding.subjectChapterIcon.setImageResource(R.drawable.math_chapter_icon)
        }else if(item.subname=="English Grammar"){
            binding.subjectChapterIcon.setImageResource(R.drawable.eng_chapter_icon)
        }else  if(item.subname=="Social Science"){
            binding.subjectChapterIcon.setImageResource(R.drawable.social_chap_icon)
        }else if(item.subname=="Urdu"){
            binding.subjectChapterIcon.setImageResource(R.drawable.urdu_icon)
       }else if(item.subname=="Bio"){
            binding.subjectChapterIcon.setImageResource(R.drawable.bio_icon)
       }
    }
}
package com.example.nafisquaisarcoachingcenter.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.HomeItemBinding
import com.example.nafisquaisarcoachingcenter.model.ChapterModel

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
        }else{
            binding.subjectChapterIcon.setImageResource(R.drawable.chapter_science_icon)
       }
    }
}
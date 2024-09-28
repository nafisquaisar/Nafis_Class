package com.example.nafisquaisarcoachingcenter.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.BoardItemCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.NoteItemCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.VideoItemCallback
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.CourseBinding
import com.example.nafisquaisarcoachingcenter.databinding.HomeItemBinding
import com.example.nafisquaisarcoachingcenter.databinding.NoteItemBinding
import com.example.nafisquaisarcoachingcenter.databinding.VideoItemBinding
import com.example.nafisquaisarcoachingcenter.model.NoteModel
import com.example.nafisquaisarcoachingcenter.model.VideoModel
import com.example.nafisquaisarcoachingcenter.model.categoryClass

class BoardViewHolder(val binding: CourseBinding, val callback: BoardItemCallback):RecyclerView.ViewHolder(binding.root){

    fun bind(item: categoryClass){
        binding.apply {
           categoryText.text=item.catText
           categoryImage.setImageResource(item.catImage)
            itemView.setOnClickListener {
                callback.onBoardClick(item, position = position)
            }
        }

    }

}

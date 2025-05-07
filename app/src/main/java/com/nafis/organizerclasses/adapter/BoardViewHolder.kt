package com.nafis.organizerclasses.adapter

import androidx.recyclerview.widget.RecyclerView
import com.nafis.organizerclasses.DIffUtilCallBack.BoardItemCallback
import com.nafis.organizerclasses.databinding.CourseBinding
import com.nafis.organizerclasses.model.categoryClass

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

package com.example.nafisquaisarcoachingcenter.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.NoteItemCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.VideoItemCallback
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.NoteItemBinding
import com.example.nafisquaisarcoachingcenter.databinding.VideoItemBinding
import com.example.nafisquaisarcoachingcenter.model.NoteModel
import com.example.nafisquaisarcoachingcenter.model.VideoModel

class VideoViewHolder(val binding: VideoItemBinding, val callback: VideoItemCallback):RecyclerView.ViewHolder(binding.root){

    fun bind(item: VideoModel){
        binding.apply {
            ChapterName.text=item.title
            Date.text=item.date.toString()
            time.text=item.time.toString()
            pdfIcon.setImageResource(R.drawable.current_photo)
            itemView.setOnClickListener {
                callback.onNoteClick(item, position = position)
            }
        }
    }

}

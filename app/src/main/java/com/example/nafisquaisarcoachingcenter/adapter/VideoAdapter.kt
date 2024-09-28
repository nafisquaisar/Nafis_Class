package com.example.nafisquaisarcoachingcenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DiffNoteCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DiffVideoCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.NoteItemCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.VideoItemCallback
import com.example.nafisquaisarcoachingcenter.databinding.VideoItemBinding
import com.example.nafisquaisarcoachingcenter.model.VideoModel

class VideoAdapter(var callback:VideoItemCallback): ListAdapter<VideoModel, VideoViewHolder>(DiffVideoCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
    var binding=VideoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
     return VideoViewHolder(binding,callback)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
            val currItem=getItem(position)
            holder.bind(currItem)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
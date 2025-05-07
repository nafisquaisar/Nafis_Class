package com.nafis.organizerclasses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nafis.organizerclasses.DIffUtilCallBack.DiffVideoCallback
import com.nafis.organizerclasses.DIffUtilCallBack.VideoItemCallback
import com.nafis.organizerclasses.databinding.VideoItemBinding
import com.nafis.organizerclasses.model.VideoModel

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
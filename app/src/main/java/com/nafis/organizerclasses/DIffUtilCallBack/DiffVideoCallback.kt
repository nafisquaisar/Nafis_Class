package com.nafis.organizerclasses.DIffUtilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.nafis.organizerclasses.model.VideoModel

class DiffVideoCallback :DiffUtil.ItemCallback<VideoModel>() {
    override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
               return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
              return oldItem.title==newItem.title &&
                           oldItem.clasname==newItem.clasname &&
                           oldItem.subname==newItem.subname &&
                           oldItem.time==newItem.time &&
                           oldItem.chapname==newItem.chapname &&
                           oldItem.date==newItem.date &&
                           oldItem.videoUrl==newItem.videoUrl&&
                           oldItem.des==newItem.des
    }
}
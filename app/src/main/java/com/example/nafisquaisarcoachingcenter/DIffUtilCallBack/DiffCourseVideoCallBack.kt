package com.example.nafisquaisarcoachingcenter.DIffUtilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.example.nafisquaisarcoachingcenter.model.Video


class DiffCourseVideoCallBack : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem.videoId == newItem.videoId
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem.videoTitle == newItem.videoTitle &&
                oldItem.videoUrl == newItem.videoUrl &&
                oldItem.date == newItem.date &&
                oldItem.time == newItem.time &&
                oldItem.des == newItem.des

    }
}
package com.example.nafisquaisarcoachingcenter.DIffUtilCallBack

import com.example.nafisquaisarcoachingcenter.model.VideoModel

interface VideoItemCallback {
    fun onNoteClick(item: VideoModel,position:Int)
}
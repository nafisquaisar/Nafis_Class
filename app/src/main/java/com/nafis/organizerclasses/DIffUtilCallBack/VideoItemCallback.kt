package com.nafis.organizerclasses.DIffUtilCallBack

import com.nafis.organizerclasses.model.VideoModel

interface VideoItemCallback {
    fun onNoteClick(item: VideoModel,position:Int)
}
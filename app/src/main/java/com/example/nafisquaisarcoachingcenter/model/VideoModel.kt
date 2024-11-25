package com.example.nafisquaisarcoachingcenter.model

data class VideoModel(
    val id: Long =0L,
    val title:String?=null,
    val des:String?=null,
    var date:String?=null,
    var time:String?=null,
    val chapname:String? =null,
    var clasname:String?=null,
    var subname:String?=null,
    var videoUrl:String?=null
)

package com.nafis.organizerclasses.model

data class VideoModel(
    val id: Long =0L,
    val title:String?=null,
    val des:String?=null,
    val date:String?=null,
    val time:String?=null,
    val chapname:String? =null,
    var clasname:String?=null,
    var subname:String?=null,
    var videoUrl:String?=null
)

package com.nafis.organizerclasses.model

data class NoteModel(
    val id:Int=0,
    val title:String?=null,
    var date:String?=null,
    val chapname:String? =null,
    var clasname:String?=null,
    var subname:String?=null,
    var pdfUrl:String?=null
)

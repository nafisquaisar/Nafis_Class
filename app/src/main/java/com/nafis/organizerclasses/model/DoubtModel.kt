package com.nafis.organizerclasses.model

data class DoubtModel(
    val doubtId:String?="",
    val userUid:String?="",
    val userName:String?="",
    val userImgUrl:String?="",
    val userEmail:String?="",
    val studQuesImgUrl:String?="",
    val studQuesTitle:String?="",
    val studQuesDesc:String?="",
    val teachAnsImgUrl:String?="",
    val teachAnsDesc:String?="",
    val timestamp: Long = 0L ,
    val status: Boolean? = null ,
    val solved:Boolean=false
)

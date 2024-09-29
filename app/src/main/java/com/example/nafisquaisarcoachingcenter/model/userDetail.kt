package com.example.nafisquaisarcoachingcenter.model

data class userDetail(
    var id: String,
    var Name:String,
    var Email:String,
    var Number:String,
    var course:String,
    var ProUrl:String){
    constructor():this("","","","","","")
}

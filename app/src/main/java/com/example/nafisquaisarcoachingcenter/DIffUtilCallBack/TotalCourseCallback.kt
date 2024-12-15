package com.example.nafisquaisarcoachingcenter.DIffUtilCallBack

import com.example.nafisquaisarcoachingcenter.model.TotalCourse

interface TotalCourseCallback {
    fun onCourseClick(item: TotalCourse, position:Int)
}
package com.nafis.organizerclasses.DIffUtilCallBack

import com.nafis.organizerclasses.model.TotalCourse

interface TotalCourseCallback {
    fun onCourseClick(item: TotalCourse, position:Int)
}
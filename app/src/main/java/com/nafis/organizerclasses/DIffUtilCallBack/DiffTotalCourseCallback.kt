package com.nafis.organizerclasses.DIffUtilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.nafis.organizerclasses.model.TotalCourse

class DiffTotalCourseCallback:DiffUtil.ItemCallback<TotalCourse>() {
    override fun areItemsTheSame(oldItem: TotalCourse, newItem: TotalCourse): Boolean {
        return oldItem.courseId==newItem.courseId
    }

    override fun areContentsTheSame(oldItem: TotalCourse, newItem: TotalCourse): Boolean {
           return oldItem.courseName==newItem.courseName&&
                   oldItem.courseDesc==newItem.courseDesc &&
                   oldItem.courseAmount==newItem.courseAmount &&
                   oldItem.courseDate==newItem.courseDate &&
                   oldItem.courseImgUrl==newItem.courseImgUrl
    }

}
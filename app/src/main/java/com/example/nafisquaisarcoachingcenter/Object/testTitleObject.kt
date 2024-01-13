package com.example.nafisquaisarcoachingcenter.Object

import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.model.categoryClass
import com.example.nafisquaisarcoachingcenter.model.testcategoryClass

object testTitleObject {
    private lateinit var dataList: ArrayList<testcategoryClass>

    fun getdata():ArrayList<testcategoryClass>{
       dataList=ArrayList<testcategoryClass>()

        dataList.add(testcategoryClass(R.drawable.class10th,"Class 10th"))
        dataList.add(testcategoryClass(R.drawable.class9,"Class 9th"))
        dataList.add(testcategoryClass(R.drawable.class8th,"Class 8th"))
        dataList.add(testcategoryClass(R.drawable.class7,"Class 7th"))
        dataList.add(testcategoryClass(R.drawable.class6,"Class 6th"))
        dataList.add(testcategoryClass(R.drawable.class5,"Class 5th"))

        return dataList
    }
}
package com.example.nafisquaisarcoachingcenter.Object

import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.model.categoryClass

object homeClassObject {
    private lateinit var categoryList:ArrayList<categoryClass>
    fun getData():ArrayList<categoryClass>{

        categoryList=ArrayList<categoryClass>()
        categoryList.add(categoryClass(R.drawable.class10th,"Class 10"))
        categoryList.add(categoryClass(R.drawable.class9,"Class 9"))
        categoryList.add(categoryClass(R.drawable.class8th,"Class 8"))
        categoryList.add(categoryClass(R.drawable.class7,"Class 7"))
        categoryList.add(categoryClass(R.drawable.class6,"Class 6"))
        categoryList.add(categoryClass(R.drawable.class5,"Class 5"))

        return categoryList
    }
}
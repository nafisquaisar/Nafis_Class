package com.nafis.organizerclasses.Object

import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.model.testcategoryClass

object testTitleObject {
    private lateinit var dataList: ArrayList<testcategoryClass>

    fun getdata():ArrayList<testcategoryClass>{
       dataList=ArrayList<testcategoryClass>()

        dataList.add(testcategoryClass(R.drawable.class12,"Class 12"))
        dataList.add(testcategoryClass(R.drawable.class11,"Class 11"))
        dataList.add(testcategoryClass(R.drawable.class10th,"Class 10"))
        dataList.add(testcategoryClass(R.drawable.class9,"Class 9"))
        dataList.add(testcategoryClass(R.drawable.class8th,"Class 8"))
        dataList.add(testcategoryClass(R.drawable.class7,"Class 7"))
        dataList.add(testcategoryClass(R.drawable.class6,"Class 6"))
        dataList.add(testcategoryClass(R.drawable.class5,"Class 5"))
        dataList.add(testcategoryClass(R.drawable.class4,"Class 4"))
        dataList.add(testcategoryClass(R.drawable.class3,"Class 3"))
        dataList.add(testcategoryClass(R.drawable.class2,"Class 2"))
        dataList.add(testcategoryClass(R.drawable.class1,"Class 1"))

        return dataList
    }
}
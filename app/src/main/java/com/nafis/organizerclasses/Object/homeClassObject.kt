package com.nafis.organizerclasses.Object

import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.model.categoryClass

object homeClassObject {
    private lateinit var categoryList:ArrayList<categoryClass>
    fun getData():ArrayList<categoryClass>{

        categoryList=ArrayList<categoryClass>()
        categoryList.add(categoryClass(R.drawable.class12,"Class 12"))
        categoryList.add(categoryClass(R.drawable.class11,"Class 11"))
        categoryList.add(categoryClass(R.drawable.class10th,"Class 10"))
        categoryList.add(categoryClass(R.drawable.class9,"Class 9"))
        categoryList.add(categoryClass(R.drawable.class8th,"Class 8"))
        categoryList.add(categoryClass(R.drawable.class7,"Class 7"))
        categoryList.add(categoryClass(R.drawable.class6,"Class 6"))
        categoryList.add(categoryClass(R.drawable.class5,"Class 5"))
        categoryList.add(categoryClass(R.drawable.class4,"Class 4"))
        categoryList.add(categoryClass(R.drawable.class3,"Class 3"))
        categoryList.add(categoryClass(R.drawable.class2,"Class 2"))
        categoryList.add(categoryClass(R.drawable.class1,"Class 1"))

        return categoryList
    }
}
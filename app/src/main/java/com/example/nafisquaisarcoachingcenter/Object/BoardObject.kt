package com.example.nafisquaisarcoachingcenter.Object

import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.model.categoryClass

object BoardObject {
    private lateinit var categoryList:ArrayList<categoryClass>
    fun getData():ArrayList<categoryClass>{

        categoryList=ArrayList<categoryClass>()
        categoryList.add(categoryClass(R.drawable.bihar_board,"Bihar Board"))
        categoryList.add(categoryClass(R.drawable.cbse_icon,"CBSE"))
        categoryList.add(categoryClass(R.drawable.mpboardicon,"MP Board"))


        return categoryList
    }
}
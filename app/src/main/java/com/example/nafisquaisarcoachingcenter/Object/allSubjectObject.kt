package com.example.nafisquaisarcoachingcenter.Object

import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.model.SubModel
import com.example.nafisquaisarcoachingcenter.model.categoryClass

object allSubjectObject {
    private lateinit var dataList: ArrayList<SubModel>

    fun getSubjectData(className:String):ArrayList<SubModel>{

        dataList=ArrayList<SubModel>()
        dataList.add(SubModel(R.drawable.sciencelogo,"Science",className))
        dataList.add(SubModel(R.drawable.mathsubjectlogo,"Math",className))
        dataList.add(SubModel(R.drawable.physicsubjectlogo,"Physics",className))
        dataList.add(SubModel(R.drawable.chemistrysubjectlogo,"Chemistry",className))

        return dataList
    }
}
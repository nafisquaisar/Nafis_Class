package com.nafis.organizerclasses.Object

import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.model.SubModel

object allSubjectObject {
    private lateinit var dataList: ArrayList<SubModel>

    fun getSubjectData(className:String ,BoardName:String?=""):ArrayList<SubModel>{

        dataList=ArrayList<SubModel>()

        when(className){
            "Class 11", "Class 12"->{
                dataList.add(SubModel(R.drawable.mathsubjectlogo,"Math",className))
                dataList.add(SubModel(R.drawable.english,"English Grammar",className))
                dataList.add(SubModel(R.drawable.physicsubjectlogo,"Physics",className))
                dataList.add(SubModel(R.drawable.chemistrysubjectlogo,"Chemistry",className))
                dataList.add(SubModel(R.drawable.bio,"Bio",className))
            }
            "Class 10","Class 9"->{
                        dataList.add(SubModel(R.drawable.mathsubjectlogo,"Math",className))
                        dataList.add(SubModel(R.drawable.english,"English Grammar",className))
                        dataList.add(SubModel(R.drawable.social,"Social Science",className))
                        dataList.add(SubModel(R.drawable.sciencelogo,"Science",className))
                        dataList.add(SubModel(R.drawable.urdu,"Urdu",className))
            }
            "Class 8","Class 7","Class 6","Class 5", "Class 4", "Class 3", "Class 2","Class 1"->{
                dataList.add(SubModel(R.drawable.sciencelogo,"Science",className))
                dataList.add(SubModel(R.drawable.english,"English Grammar",className))
                dataList.add(SubModel(R.drawable.social,"Social Science",className))
                dataList.add(SubModel(R.drawable.mathsubjectlogo,"Math",className))
            }
        }


        return dataList
    }
}
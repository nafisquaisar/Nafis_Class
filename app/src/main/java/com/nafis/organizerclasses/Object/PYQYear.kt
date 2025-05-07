package com.nafis.organizerclasses.Object

import com.nafis.organizerclasses.model.NoteModel

object PYQYear {
    private lateinit var dataList: ArrayList<NoteModel>

    fun getPYQYear(className:String ,BoardName:String?=""):ArrayList<NoteModel>{

        dataList=ArrayList<NoteModel>()


        dataList.add(NoteModel(0,"PYQ of 2024","","",className,"" ,"" ))
        dataList.add(NoteModel(1,"PYQ of 2023","","",className,"" ,"" ))
        dataList.add(NoteModel(2,"PYQ of 2022","","",className,"" ,"" ))
        dataList.add(NoteModel(3,"PYQ of 2021","","",className,"" ,"" ))
        dataList.add(NoteModel(4,"PYQ of 2020","","",className,"" ,"" ))
        dataList.add(NoteModel(5,"PYQ of 2019","","",className,"" ,"" ))



        return dataList
    }
}
package com.example.nafisquaisarcoachingcenter.DIffUtilCallBack

import com.example.nafisquaisarcoachingcenter.model.NoteModel
import com.example.nafisquaisarcoachingcenter.model.categoryClass

interface BoardItemCallback {
    fun onBoardClick(item: categoryClass,position:Int)
}
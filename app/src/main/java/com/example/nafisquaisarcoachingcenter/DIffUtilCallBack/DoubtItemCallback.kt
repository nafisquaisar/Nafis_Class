package com.example.nafisquaisarcoachingcenter.DIffUtilCallBack

import com.example.nafisquaisarcoachingcenter.model.DoubtModel
import com.example.nafisquaisarcoachingcenter.model.NoteModel
import com.example.nafisquaisarcoachingcenter.model.categoryClass

interface DoubtItemCallback {
    fun onBoardClick(item: DoubtModel,position:Int)
}
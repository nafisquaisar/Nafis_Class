package com.example.nafisquaisarcoachingcenter.DIffUtilCallBack

import com.example.nafisquaisarcoachingcenter.model.NoteModel

interface NoteItemCallback {
    fun onNoteClick(item: NoteModel,position:Int)
}
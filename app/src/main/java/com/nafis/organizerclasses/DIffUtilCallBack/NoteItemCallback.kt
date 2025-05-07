package com.nafis.organizerclasses.DIffUtilCallBack

import com.nafis.organizerclasses.model.NoteModel

interface NoteItemCallback {
    fun onNoteClick(item: NoteModel,position:Int)
}
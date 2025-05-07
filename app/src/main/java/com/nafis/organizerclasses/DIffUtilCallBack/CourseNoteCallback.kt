package com.nafis.organizerclasses.DIffUtilCallBack

import com.nafis.organizerclasses.model.Note


interface CourseNoteCallback {
    fun onNoteClick(item: Note)

}
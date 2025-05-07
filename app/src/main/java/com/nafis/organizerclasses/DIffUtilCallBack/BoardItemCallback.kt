package com.nafis.organizerclasses.DIffUtilCallBack

import com.nafis.organizerclasses.model.categoryClass

interface BoardItemCallback {
    fun onBoardClick(item: categoryClass,position:Int)
}
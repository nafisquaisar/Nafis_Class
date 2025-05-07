package com.nafis.organizerclasses.DIffUtilCallBack

import com.nafis.organizerclasses.model.DoubtModel

interface DoubtItemCallback {
    fun onBoardClick(item: DoubtModel,position:Int)
}
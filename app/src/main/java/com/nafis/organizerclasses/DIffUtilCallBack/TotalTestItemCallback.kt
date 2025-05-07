package com.nafis.organizerclasses.DIffUtilCallBack

import com.nafis.organizerclasses.Object.TestObject

interface TotalTestItemCallback {
    fun onTotalTestClick(item: TestObject, position:Int)
    fun onShareTest(item: TestObject)
    fun markIsComplete(item: TestObject)

}
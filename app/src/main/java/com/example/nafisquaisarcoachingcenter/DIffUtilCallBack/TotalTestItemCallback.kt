package com.example.nafisquaisarcoachingcenter.DIffUtilCallBack

import com.example.nafisquaisarcoachingcenter.Object.TestObject

interface TotalTestItemCallback {
    fun onTotalTestClick(item: TestObject, position:Int)
    fun onShareTest(item: TestObject)
    fun markIsComplete(item: TestObject)

}
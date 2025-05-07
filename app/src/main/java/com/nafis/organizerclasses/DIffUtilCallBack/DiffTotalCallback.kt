package com.nafis.organizerclasses.DIffUtilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.nafis.organizerclasses.Object.TestObject

class DiffTotalCallback : DiffUtil.ItemCallback<TestObject>() {
    override fun areItemsTheSame(oldItem: TestObject, newItem: TestObject): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TestObject, newItem: TestObject): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.noOfQues == newItem.noOfQues &&
                oldItem.totalMark == newItem.totalMark &&
                oldItem.totalTime == newItem.totalTime &&
                oldItem.classname == newItem.classname &&
                oldItem.subname == newItem.subname &&
                oldItem.chapname == newItem.chapname

    }
}

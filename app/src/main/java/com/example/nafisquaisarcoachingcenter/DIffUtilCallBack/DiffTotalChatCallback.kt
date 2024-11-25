package com.example.nafisquaisarcoachingcenter.DIffUtilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.example.nafisquaisarcoachingcenter.Object.TotalChat

class DiffTotalChatCallback:DiffUtil.ItemCallback<TotalChat>() {
    override fun areItemsTheSame(oldItem: TotalChat, newItem: TotalChat): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: TotalChat, newItem: TotalChat): Boolean {
           return oldItem.title==newItem.title
                   && oldItem.time==newItem.time
                   && oldItem.noOfMsg==newItem.noOfMsg
    }

}
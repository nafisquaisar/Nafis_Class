package com.example.nafis.nf.organizetestcenter.Adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalTestItemCallback
import com.example.nafisquaisarcoachingcenter.Object.TestObject
import com.example.nafisquaisarcoachingcenter.databinding.NoOfTestModelBinding

class TotalTestViewHolder(val binding: NoOfTestModelBinding,private val callback:TotalTestItemCallback):RecyclerView.ViewHolder(binding.root) {
        fun bind(data:TestObject){
              binding.apply {
                      totalMark.text= "${data.totalMark} Marks | "
                      totalTime.text="${data.totalTime} Minutes"
                      noOfQues.text= "${data.noOfQues} Questions |"
                      TestTitle.text=data.title
                      itemView.setOnClickListener {
                          callback.onTotalTestClick(data,position)
                      }
              }
        }
}

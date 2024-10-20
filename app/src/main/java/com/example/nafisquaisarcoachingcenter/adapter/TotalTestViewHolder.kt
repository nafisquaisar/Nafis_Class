package com.example.nafis.nf.organizetestcenter.Adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.nafis.nf.organizetestcenter.Model.TotalNoTestModel
import com.example.nafis.nf.organizetestcenter.databinding.NoOfTestModelBinding

class TotalTestViewHolder(val binding: NoOfTestModelBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data:TotalNoTestModel){
              binding.apply {
                      totalMark.text=data.totalMark
                      totalTime.text="${data.totalTime} Minute"
                      noOfQues.text=data.noOfQues
                      TestTitle.text=data.title
              }
        }
}

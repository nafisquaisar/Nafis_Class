package com.example.nafisquaisarcoachingcenter.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalChatCallback
import com.example.nafisquaisarcoachingcenter.Object.TotalChat
import com.example.nafisquaisarcoachingcenter.databinding.ChatItemBinding

class TotalChatViewHolder(var binding: ChatItemBinding, var callback: TotalChatCallback):RecyclerView.ViewHolder(binding.root) {
      fun bind(item:TotalChat){
          binding.title.text=item.title
          binding.showTime.text=item.time
          binding.showNoOfMsg.text=item.noOfMsg

          itemView.setOnClickListener {
               callback.onTotalChatClick(item,position)
          }
      }
}

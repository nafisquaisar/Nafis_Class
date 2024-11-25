package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DiffTotalChatCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalChatCallback
import com.example.nafisquaisarcoachingcenter.Object.TotalChat
import com.example.nafisquaisarcoachingcenter.databinding.ChatItemBinding

class TotalChatAdapter(var context: Context, var callback: TotalChatCallback, var list: List<TotalChat>): ListAdapter<TotalChat, TotalChatViewHolder>(DiffTotalChatCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TotalChatViewHolder {
        var binding=ChatItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return TotalChatViewHolder(binding,callback)
    }

    override fun onBindViewHolder(holder: TotalChatViewHolder, position: Int) {
         var model=list[position]
        holder.bind(model)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
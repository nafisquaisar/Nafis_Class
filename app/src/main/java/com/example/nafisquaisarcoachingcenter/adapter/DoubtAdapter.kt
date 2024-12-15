package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import android.graphics.ColorSpace.Model
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DiffDoubtCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DoubtActionCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.DoubtItemCallback
import com.example.nafisquaisarcoachingcenter.databinding.DoubtItemCardBinding
import com.example.nafisquaisarcoachingcenter.model.DoubtModel
import com.example.nafisquaisarcoachingcenter.model.categoryClass

class DoubtAdapter(
    private var context: Context,
    var callback: DoubtItemCallback,
    val doubtActionCallback: DoubtActionCallback
): ListAdapter<DoubtModel, DoubtViewHolder>(
    DiffDoubtCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoubtViewHolder {
    var binding= DoubtItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
     return DoubtViewHolder(context,binding,callback,doubtActionCallback)
    }

    override fun onBindViewHolder(holder: DoubtViewHolder, position: Int) {
            val currItem=getItem(position)
            holder.bind(currItem)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}
package com.nafis.organizerclasses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nafis.organizerclasses.DIffUtilCallBack.DiffDoubtCallback
import com.nafis.organizerclasses.DIffUtilCallBack.DoubtActionCallback
import com.nafis.organizerclasses.DIffUtilCallBack.DoubtItemCallback
import com.nafis.organizerclasses.databinding.DoubtItemCardBinding
import com.nafis.organizerclasses.model.DoubtModel

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
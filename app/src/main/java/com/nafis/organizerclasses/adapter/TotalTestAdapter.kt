package com.nafis.nafis.nf.organizetestcenter.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nafis.organizerclasses.DIffUtilCallBack.DiffTotalCallback
import com.nafis.organizerclasses.DIffUtilCallBack.TotalTestItemCallback
import com.nafis.organizerclasses.Object.TestObject
import com.nafis.organizerclasses.databinding.NoOfTestModelBinding


class TotalTestAdapter(val context: Context,val list:ArrayList<TestObject>,var callback: TotalTestItemCallback):ListAdapter<TestObject,TotalTestViewHolder>(DiffTotalCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TotalTestViewHolder {
        val view= NoOfTestModelBinding.inflate(LayoutInflater.from(context),parent,false)
        return TotalTestViewHolder(view,callback,context)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: TotalTestViewHolder, position: Int) {
        if (list.isNotEmpty() && position < list.size) {
            val model = list[position]
            holder.bind(model)
        } else {
            Log.e("TotalTestAdapter", "List is empty or position is out of bounds: position=$position, listSize=${list.size}")
        }
    }

}
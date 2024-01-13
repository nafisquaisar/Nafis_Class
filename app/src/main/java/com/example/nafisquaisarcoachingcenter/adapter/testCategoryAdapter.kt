package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.adapter.testCategoryAdapter.*
import com.example.nafisquaisarcoachingcenter.databinding.FragmentTestBinding
import com.example.nafisquaisarcoachingcenter.databinding.TestRecylerViewBinding
import com.example.nafisquaisarcoachingcenter.model.testcategoryClass
import com.example.nafisquaisarcoachingcenter.adapter.testCategoryAdapter.MyTestViewHolder as MyTestViewHolder1

class testCategoryAdapter(var dataList: ArrayList<testcategoryClass>,var requireActivity: FragmentActivity) : RecyclerView.Adapter<MyTestViewHolder1>() {

    // inner class for getting view from test layout
    inner class MyTestViewHolder(var binding: TestRecylerViewBinding):RecyclerView.ViewHolder(binding.root) {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTestViewHolder1 {
       return MyTestViewHolder(TestRecylerViewBinding.inflate(LayoutInflater.from(requireActivity),parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyTestViewHolder1, position: Int) {
        var datalist=dataList[position]
        holder.binding.logoOfTitleTest.setImageResource(datalist.logo)
        holder.binding.TitleOfTestLayout.setText(datalist.titleName)
    }
}
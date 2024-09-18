package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.adapter.testCategoryAdapter.*
import com.example.nafisquaisarcoachingcenter.coursecclass.class10th
import com.example.nafisquaisarcoachingcenter.coursecclass.class5th
import com.example.nafisquaisarcoachingcenter.coursecclass.class6th
import com.example.nafisquaisarcoachingcenter.coursecclass.class7th
import com.example.nafisquaisarcoachingcenter.coursecclass.class8th
import com.example.nafisquaisarcoachingcenter.coursecclass.class9th
import com.example.nafisquaisarcoachingcenter.databinding.FragmentTestBinding
import com.example.nafisquaisarcoachingcenter.databinding.TestRecylerViewBinding
import com.example.nafisquaisarcoachingcenter.fragment.Home
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

        holder.binding.testClassBtn.setOnClickListener{
            if(position==0){
                var intent = Intent(requireActivity,class10th::class.java)
                requireActivity.startActivity(intent)
            }else if (position==1) {
                var intent = Intent(requireActivity, class9th::class.java)
                requireActivity.startActivity(intent)
            }else if(position==2){
                var intent = Intent(requireActivity, class8th::class.java)
                requireActivity.startActivity(intent)
            }else if(position==3){
                var intent = Intent(requireActivity, class7th::class.java)
                requireActivity.startActivity(intent)
            }else if(position==4){
                var intent = Intent(requireActivity, class6th::class.java)
                requireActivity.startActivity(intent)
            }else{
                var intent = Intent(requireActivity, class5th::class.java)
                requireActivity.startActivity(intent)
            }
        }


    }
}
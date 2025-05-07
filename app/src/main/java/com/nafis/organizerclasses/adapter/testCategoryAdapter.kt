package com.nafis.organizerclasses.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nafis.organizerclasses.adapter.testCategoryAdapter.*
import com.nafis.organizerclasses.activity.ClassMainActivity
import com.nafis.organizerclasses.databinding.TestRecylerViewBinding
import com.nafis.organizerclasses.model.testcategoryClass
import com.nafis.organizerclasses.adapter.testCategoryAdapter.MyTestViewHolder as MyTestViewHolder1

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

        holder.itemView.setOnClickListener {
            val intent = Intent(requireActivity, ClassMainActivity::class.java)
            intent.putExtra("className",datalist.titleName)
            intent.putExtra("test","Test")
            requireActivity.startActivity(intent)
        }


    }
}
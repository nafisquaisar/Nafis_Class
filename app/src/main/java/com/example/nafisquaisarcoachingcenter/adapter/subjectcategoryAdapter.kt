package com.example.nafisquaisarcoachingcenter.adapter

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.AllSubjectLayoutBinding
import com.example.nafisquaisarcoachingcenter.model.categoryClass
import com.example.nafisquaisarcoachingcenter.adapter.subjectcategoryAdapter.MySubjectCategory
import com.example.nafisquaisarcoachingcenter.fragment.ChapterFragment
import com.example.nafisquaisarcoachingcenter.model.SubModel

class subjectcategoryAdapter(var dataList: ArrayList<SubModel>,var requireActivity:Activity): RecyclerView.Adapter<MySubjectCategory>() {
    inner class MySubjectCategory(var binding: AllSubjectLayoutBinding):RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySubjectCategory {
        var view=MySubjectCategory(AllSubjectLayoutBinding.inflate(LayoutInflater.from(requireActivity),parent,false))
        return view
    }

    override fun getItemCount(): Int {
       return dataList.size
    }

    override fun onBindViewHolder(holder: MySubjectCategory, position: Int) {

        var dataList=dataList[position]

        holder.binding.allSubjectLogo.setImageResource(dataList.catImage)
        holder.binding.allSubjectTitle.setText(
            dataList.SubjText)

       holder.itemView.setOnClickListener {
           val fragmentManager = (requireActivity as AppCompatActivity).supportFragmentManager
           val transaction = fragmentManager.beginTransaction()
           val chapterFragment = ChapterFragment(dataList.SubjText,dataList.className) // Replace with your actual subFragment class



           // Replace the current fragment with the new one
           transaction.replace(R.id.wrapper, chapterFragment)
           transaction.addToBackStack(null) // Optional: to add this transaction to the back stack
           transaction.commit()
       }
    }
}
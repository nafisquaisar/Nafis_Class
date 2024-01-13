package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.coursecclass.class10th
import com.example.nafisquaisarcoachingcenter.coursecclass.class5th
import com.example.nafisquaisarcoachingcenter.coursecclass.class6th
import com.example.nafisquaisarcoachingcenter.coursecclass.class7th
import com.example.nafisquaisarcoachingcenter.coursecclass.class8th
import com.example.nafisquaisarcoachingcenter.coursecclass.class9th
import com.example.nafisquaisarcoachingcenter.databinding.CourseBinding
import com.example.nafisquaisarcoachingcenter.model.categoryClass


class categoryAdapter(var categoryList: ArrayList<categoryClass>,var requireActivity: FragmentActivity): RecyclerView.Adapter<categoryAdapter.MyCategoryViewHolder>() {
    class MyCategoryViewHolder(var binding: CourseBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoryViewHolder {
      return MyCategoryViewHolder(CourseBinding.inflate(LayoutInflater.from(parent.context),parent,false ))
    }

    override fun getItemCount(): Int =categoryList.size

    override fun onBindViewHolder(holder: MyCategoryViewHolder, position: Int)
    {
        var datalist=categoryList[position]
        holder.binding.categoryImage.setImageResource(datalist.catImage)
        holder.binding.categoryText.setText(datalist.catText)

        holder.binding.coursebtn.setOnClickListener{
           if(position==0){
               var intent = Intent(requireActivity, class10th::class.java)
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
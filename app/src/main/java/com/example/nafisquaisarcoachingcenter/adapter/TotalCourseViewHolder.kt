package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalCourseCallback
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.model.TotalCourse
import com.example.nafisquaisarcoachingcenter.databinding.CourseItemBinding

class TotalCourseViewHolder(var binding: CourseItemBinding, var callback: TotalCourseCallback,
  var context: Context
):RecyclerView.ViewHolder(binding.root) {
    fun bind(item:TotalCourse){
        binding.apply {
            showtitleOfCourse.text=item.courseName
            showPriceOfCourse.text="₹ ${item.courseAmount}"
            showOfferPriceOfCourse.text="₹ ${item.offerAmount}"
            showPriceOfCourse.paintFlags = showPriceOfCourse.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            showtitleOfCourse.isSelected=true
            Glide.with(context)
                .load(item.courseImgUrl)
                .placeholder(R.drawable.biharboardbanner)
                .into(courseBanner)
        }

        itemView.setOnClickListener {
            callback.onCourseClick(item, position = position)
        }

    }
}
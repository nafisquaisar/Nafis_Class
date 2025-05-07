package com.nafis.organizerclasses.adapter

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nafis.organizerclasses.DIffUtilCallBack.TotalCourseCallback
import com.nafis.organizerclasses.model.TotalCourse
import com.nafis.organizerclasses.databinding.CourseItemBinding

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
                .into(courseBanner)
        }

        itemView.setOnClickListener {
            callback.onCourseClick(item, position = position)
        }

    }
}

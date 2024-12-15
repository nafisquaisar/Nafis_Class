package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.nafis.nf2024.organizeradminpanel.DiffutilCallBack.SubjectCallback
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.AllSubjectLayoutBinding
import com.example.nafisquaisarcoachingcenter.model.Subject

class CourseSubjectViewHolder(
    var binding:AllSubjectLayoutBinding, var context: Context
    , var callback: SubjectCallback
):RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Subject){
        binding.apply {
            allSubjectTitle.text=item.subjectName
            when(item.subjectName){
                "Science"->{
                    allSubjectLogo.setImageResource(R.drawable.sciencelogo)
                }
                "Chemistry"->{
                    allSubjectLogo.setImageResource(R.drawable.chemistrysubjectlogo)
                }
                else->{
                    allSubjectLogo.setImageResource(R.drawable.course)
                }
            }
        }

        itemView.setOnClickListener {
            callback.onSubjectClick(item)
        }

    }

}

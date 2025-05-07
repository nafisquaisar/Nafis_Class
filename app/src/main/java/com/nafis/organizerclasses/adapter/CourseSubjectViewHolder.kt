package com.nafis.organizerclasses.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.nafis.nafis.nf2024.organizeradminpanel.DiffutilCallBack.SubjectCallback
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.databinding.AllSubjectLayoutBinding
import com.nafis.organizerclasses.model.Subject

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

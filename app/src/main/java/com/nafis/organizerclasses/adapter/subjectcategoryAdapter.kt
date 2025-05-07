package com.nafis.organizerclasses.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.databinding.AllSubjectLayoutBinding
import com.nafis.organizerclasses.adapter.subjectcategoryAdapter.MySubjectCategory
import com.nafis.organizerclasses.fragment.ChapterFragment
import com.nafis.organizerclasses.fragment.PYQViewerFragment
import com.nafis.organizerclasses.model.SubModel

class subjectcategoryAdapter(private var dataList: ArrayList<SubModel>, var requireActivity:Activity,private var BoardName:String?,private var Test:String?=""): RecyclerView.Adapter<MySubjectCategory>() {
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
           val chapterFragment = ChapterFragment(dataList.SubjText,dataList.className,Test) // Replace with your actual subFragment class


          if(BoardName!=null &&BoardName=="Bihar Board" || BoardName=="CBSE" ||BoardName=="MP Board"){
              // Replace the current fragment with the new one
              transaction.replace(R.id.wrapper, PYQViewerFragment(dataList.className,dataList.SubjText,"",BoardName))
              transaction.addToBackStack("PYQFragment") // Optional: to add this transaction to the back stack
              transaction.commit()
          }else{
              // Replace the current fragment with the new one
              transaction.replace(R.id.wrapper, chapterFragment)
              transaction.addToBackStack("ChapterFragment")
              transaction.commit()
          }

       }
    }
}
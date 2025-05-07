package com.nafis.organizerclasses.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafis.organizerclasses.databinding.CourseBinding
import com.nafis.organizerclasses.model.categoryClass
import com.nafis.organizerclasses.activity.ClassMainActivity

class categoryAdapter(
    private var categoryList: ArrayList<categoryClass>,
    private val context: Context,private var BoardName:String=""
) : RecyclerView.Adapter<categoryAdapter.MyCategoryViewHolder>() {

    inner class MyCategoryViewHolder(val binding: CourseBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.coursebtn.setOnClickListener {
                val datalist = categoryList[adapterPosition]
                val intent = Intent(context, ClassMainActivity::class.java)
                intent.putExtra("className", datalist.catText)
                intent.putExtra("BoardName", BoardName)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoryViewHolder {
        val binding = CourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyCategoryViewHolder, position: Int) {
        val datalist = categoryList[position]
        holder.binding.categoryImage.setImageResource(datalist.catImage)
        holder.binding.categoryText.text = datalist.catText
    }

    fun filterfun(categoryList: ArrayList<categoryClass>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }
}

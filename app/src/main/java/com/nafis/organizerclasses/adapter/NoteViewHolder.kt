package com.nafis.organizerclasses.adapter

import androidx.recyclerview.widget.RecyclerView
import com.nafis.organizerclasses.DIffUtilCallBack.NoteItemCallback
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.databinding.NoteItemBinding
import com.nafis.organizerclasses.databinding.PyqItemBinding
import com.nafis.organizerclasses.model.NoteModel

class NoteViewHolder(
    private val binding: Any, // Use Any to allow both PyqItemBinding and NoteItemBinding
    private val callback: NoteItemCallback,
) : RecyclerView.ViewHolder(
    when (binding) {
        is PyqItemBinding -> binding.root
        is NoteItemBinding -> binding.root
        else -> throw IllegalArgumentException("Invalid binding type")
    }
) {

    fun bind(item: NoteModel) {
        when (binding) {
            is PyqItemBinding -> {
                binding.apply {
                    yearName.text = item.title
                    yearName.isSelected = true  // This is crucial for enabling the marquee effect
                    Icon.setImageResource(R.drawable.pyq_icon)
                    itemView.setOnClickListener {
                        callback.onNoteClick(item, position = adapterPosition)
                    }
                }
            }
            is NoteItemBinding -> {
                binding.apply {
                    ChapterName.text = item.title
                    ChapterName.isSelected=true
                    date.text = item.date.toString()
                    pdfIcon.setImageResource(R.drawable.pdf_icon)
                    itemView.setOnClickListener {
                        callback.onNoteClick(item, position = adapterPosition)
                    }
                }
            }
        }
    }
}

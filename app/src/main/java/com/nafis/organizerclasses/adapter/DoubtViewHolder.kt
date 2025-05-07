package com.nafis.organizerclasses.adapter

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nafis.organizerclasses.DIffUtilCallBack.DoubtActionCallback
import com.nafis.organizerclasses.DIffUtilCallBack.DoubtItemCallback
import com.nafis.organizerclasses.DoubtActivity
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.databinding.DoubtItemCardBinding
import com.nafis.organizerclasses.model.DoubtModel

class DoubtViewHolder(
    private val context: Context,
    private val binding: DoubtItemCardBinding,
    private val callback: DoubtItemCallback,
    private val doubtActionCallback: DoubtActionCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DoubtModel) {
        binding.apply {
            studQuesTitle.text = item.studQuesTitle
            studQuesDesc.text = item.studQuesDesc
            positionId.text = (position+1).toString()

            Glide.with(context)
                .load(item.studQuesImgUrl)
                .placeholder(R.drawable.pyq_icon)
                .error(R.drawable.pyq_icon)
                .into(studQuesImg)
            warning.visibility=View.VISIBLE
            teachAnsImg.visibility = View.GONE
            teachAnsDesc.visibility = View.GONE
            solution.visibility = View.GONE
            binding.llSolution.visibility=View.GONE
            binding.solutionDownArrow.visibility=View.VISIBLE
            binding.solUpArrow.visibility=View.GONE
            if (item.teachAnsImgUrl?.isNotEmpty() == true) {
                teachAnsImg.visibility = View.VISIBLE
                teachAnsDesc.visibility = View.VISIBLE
                solution.visibility = View.VISIBLE
                teachAnsDesc.text = item.teachAnsDesc
                warning.visibility=View.GONE
                Glide.with(context)
                    .load(item.teachAnsImgUrl)
                    .placeholder(R.drawable.pyq_icon)
                    .error(R.drawable.pyq_icon)
                    .into(teachAnsImg)
            }
            setDropUpDown(item)

            // ==========set full size of the image========
            teachAnsImg.setOnClickListener {
                showImageDialog(item.teachAnsImgUrl.toString())
            }

            studQuesImg.setOnClickListener {
                showImageDialog(item.studQuesImgUrl.toString())
            }
        }


        // Handle item click
        itemView.setOnClickListener {
            callback.onBoardClick(item, position = position)
            if (binding.llSolution.visibility == View.VISIBLE) {
                binding.llSolution.visibility = View.GONE
                binding.solutionDownArrow.visibility=View.VISIBLE
                binding.solUpArrow.visibility=View.GONE
                if (item.teachAnsImgUrl?.isEmpty()==true){
                    binding.warning.visibility=View.GONE
                }
            } else {
                binding.llSolution.visibility = View.VISIBLE
                binding.solutionDownArrow.visibility=View.GONE
                binding.solUpArrow.visibility=View.VISIBLE
                if (item.teachAnsImgUrl?.isEmpty()==true){
                    binding.warning.visibility=View.VISIBLE
                }
            }

        }
        popUpMenu(item)
    }

    private fun popUpMenu(item:DoubtModel) {
        binding.moreOption.setOnClickListener {
            val popupMenu=PopupMenu(context,it)
            popupMenu.inflate(R.menu.pop_menu_doubt)

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.action_update->{
                            doubtActionCallback.onUpdateDoubt(item)
                        true
                    }
                    R.id.action_delete->{
                        item.doubtId?.let { it1 -> deleteDoubt(it1) }
                        true
                    }
                    else-> false
                }
            }


            popupMenu.show()
        }
    }

    private fun deleteDoubt(doubtId: String) {
        (context as? DoubtActivity)?.deleteDoubtFromFirebase(doubtId)
    }




    private fun setDropUpDown(item: DoubtModel) {
           binding.solutionDownArrow.setOnClickListener {
              binding.llSolution.visibility=View.VISIBLE
              binding.solutionDownArrow.visibility=View.GONE
              binding.solUpArrow.visibility=View.VISIBLE
                 if (item.teachAnsImgUrl?.isEmpty()==true){
                     binding.warning.visibility=View.VISIBLE
                 }
           }

          binding.solUpArrow.setOnClickListener {
              binding.llSolution.visibility=View.GONE
              binding.solutionDownArrow.visibility=View.VISIBLE
              binding.solUpArrow.visibility=View.GONE
               if (item.teachAnsImgUrl?.isEmpty()==true){
                     binding.warning.visibility=View.GONE
                 }
          }
    }



    //===============show Image Dialog Image in full size if we want then click on the photo ==================
    private fun showImageDialog(QuesImgUrl: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.image_view_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.show()

        // Set the image in the dialog
        val fullImageView = dialog.findViewById<ImageView>(R.id.fullImageView)
        Glide.with(context)
            .load(QuesImgUrl)
            .placeholder(R.drawable.pyq_icon)
            .into(fullImageView)
        // Dismiss the dialog when the image is clicked
        fullImageView.setOnClickListener {
            dialog.dismiss()
        }
    }



}

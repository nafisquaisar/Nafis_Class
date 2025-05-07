package com.nafis.nafis.nf.organizetestcenter.Adapter

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.nafis.organizerclasses.DIffUtilCallBack.TotalTestItemCallback
import com.nafis.organizerclasses.Object.TestObject
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.databinding.NoOfTestModelBinding

class TotalTestViewHolder(val binding: NoOfTestModelBinding,private val callback:TotalTestItemCallback,
           val context: Context
  ):RecyclerView.ViewHolder(binding.root) {
        fun bind(data:TestObject){
              binding.apply {
                      totalMark.text= "${data.totalMark} Marks | "
                      totalTime.text="${data.totalTime} Minutes"
                      noOfQues.text= "${data.noOfQues} Questions |"
                      TestTitle.text=data.title
                  if(data.isCompleted==true){
                      binding.checkCompleteTest.visibility=View.VISIBLE
                  }else{
                      binding.checkCompleteTest.visibility=View.GONE
                  }
                      itemView.setOnClickListener {
                          callback.onTotalTestClick(data,position)
                      }

                    clickmore.setOnClickListener {
                        popDialog(data)
                    }

              }
        }

    private fun popDialog(data: TestObject) {
        val dialog = PopupMenu(context, binding.clickmore)
        dialog.inflate(R.menu.pop_menu_more)

        // Show the PopupMenu
        dialog.show()

        val markDoneMenuItem = dialog.menu.findItem(R.id.markDone)



        if (data.isCompleted == true) {
            markDoneMenuItem.title = "Mark as Incomplete"
        } else {
            markDoneMenuItem.title = "Mark as Complete"
        }


        dialog.setOnMenuItemClickListener {
            when (it.itemId) {
//                R.id.share -> {
//                    callback.onShareTest(data)
//                    true
//                }
                R.id.markDone -> {
                    // Handle the "markDone" functionality here
                    markTestAsDone(data)
                    true
                }
                else -> false
            }
        }
    }

    private fun markTestAsDone(data: TestObject) {

        callback.markIsComplete(item = data)


    }
}

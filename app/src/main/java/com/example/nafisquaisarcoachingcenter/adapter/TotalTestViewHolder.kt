package com.example.nafis.nf.organizetestcenter.Adapter

import android.content.Context
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalTestItemCallback
import com.example.nafisquaisarcoachingcenter.Object.TestObject
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.NoOfTestModelBinding

class TotalTestViewHolder(val binding: NoOfTestModelBinding,private val callback:TotalTestItemCallback,
           val context: Context
  ):RecyclerView.ViewHolder(binding.root) {
        fun bind(data:TestObject){
              binding.apply {
                      totalMark.text= "${data.totalMark} Marks | "
                      totalTime.text="${data.totalTime} Minutes"
                      noOfQues.text= "${data.noOfQues} Questions |"
                      TestTitle.text=data.title
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

        dialog.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.share -> {
                    callback.onShareTest(data)
                    true
                }
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

        Toast.makeText(context, "Test marked as done: ${data.title}", Toast.LENGTH_SHORT).show()

    }
}

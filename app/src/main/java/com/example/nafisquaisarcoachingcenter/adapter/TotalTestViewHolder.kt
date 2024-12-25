package com.example.nafis.nf.organizetestcenter.Adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalTestItemCallback
import com.example.nafisquaisarcoachingcenter.Object.TestObject
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.NoOfTestModelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

        callback.markIsComplete(item = data)


    }
}

package com.nafis.organizerclasses.Object

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.nafis.organizerclasses.R

object progress {
    private var dialog: AlertDialog?=null

    fun show(context: Context){
        dialog= AlertDialog.Builder(context)
            .setView(R.layout.progress_dialog)
            .setCancelable(true)
            .create()
        dialog!!.show()
    }
    fun hide(){
      dialog?.hide()
    }
    fun toastShow(context: Context,message:String){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

}
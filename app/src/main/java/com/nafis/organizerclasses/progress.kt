package com.nafis.organizerclasses

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.storage.UploadTask

object progress {
    private var dialog: AlertDialog?=null
    private var progressDialog: Dialog? = null

    fun show(context: Context){
        dialog= AlertDialog.Builder(context)
            .setView(R.layout.progress_dialog)
            .setCancelable(true)
            .create()
        dialog!!.show()
    }
    fun hide(){
      dialog?.dismiss()
    }
    fun ToastShow(context: Context,message:String){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

    fun showProgressDialog(context: Context, uploadTask: UploadTask) {

        progressDialog = Dialog(context)
        progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog?.setContentView(R.layout.progress_dailog)



        val window = progressDialog?.window
        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        params?.horizontalMargin = 0.1f
        window?.attributes = params

        // Find views
        val progressBar = progressDialog?.findViewById<ProgressBar>(R.id.progressShow)
        val progressPercentage = progressDialog?.findViewById<TextView>(R.id.progressPercentage)
        val status = progressDialog?.findViewById<TextView>(R.id.uploadingLabel)

        // Set initial progress
        progressBar?.progress = 0
        progressPercentage?.text = "0%"

        // Show dialog
        progressDialog?.show()

        // Update progress dynamically based on upload bytes
        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()

            // Update progress bar and percentage text
            progressBar?.progress = progress
            progressPercentage?.text = "$progress%"
        }.addOnSuccessListener {
            // Handle successful upload
            progressPercentage?.text = "100%"
            status?.text = "Completed"
            dismissProgressDialog()
        }.addOnFailureListener { exception ->
            // Handle failed upload
            progressPercentage?.text = "Upload Failed"
            dismissProgressDialog()
        }
    }


    fun dismissProgressDialog() {
        // Dismiss the dialog and reset the progressDialog reference
        progressDialog?.dismiss()
        progressDialog = null
    }

}
package com.nafis.organizerclasses

import android.app.Dialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.nafis.organizerclasses.DIffUtilCallBack.DoubtActionCallback
import com.nafis.organizerclasses.DIffUtilCallBack.DoubtItemCallback
import com.nafis.organizerclasses.adapter.DoubtAdapter
import com.nafis.organizerclasses.databinding.ActivityDoubtBinding
import com.nafis.organizerclasses.model.DoubtModel
import com.nafis.organizerclasses.model.userDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.nafis.organizerclasses.notification.FCMNotificationSender
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale
import java.util.UUID

class DoubtActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDoubtBinding
    private  var imgUri: Uri? =null
    private lateinit var studImgView: ImageView
    private lateinit var list:ArrayList<DoubtModel>
    private lateinit var adapter:DoubtAdapter
    private var selectImage=registerForActivityResult(ActivityResultContracts.GetContent()){
                imgUri=it
            if (::studImgView.isInitialized) {
                studImgView.setImageURI(imgUri)
            } else {
                Log.e("DoubtActivity", "studImgView is not initialized yet")
            }

    }


    private val doubtCallback by lazy {
             object : DoubtItemCallback{
                 override fun onBoardClick(item: DoubtModel, position: Int) {

                 }

             }
    }

    private val doubtActionCallback by lazy {
        object :DoubtActionCallback{
            override fun onUpdateDoubt(item: DoubtModel) {
                doubtAddDailog(item)
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDoubtBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val fab = binding.doubtAddBtn
        fab.setImageResource(R.drawable.baseline_add_24)
        fab.imageTintList = ContextCompat.getColorStateList(this, android.R.color.white)

        adapter = DoubtAdapter(this@DoubtActivity, callback = doubtCallback,doubtActionCallback)


        binding.backarrowbtn.setOnClickListener {
            onBackPressed()
        }

        binding.doubtAddBtn.setOnClickListener{
               doubtAddDailog()
        }
        binding.SearchCourse.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })


        fetchData()
    }

    private fun fetchData() {
        binding.progressbar.visibility = View.VISIBLE
        list= ArrayList()
        val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        val ref=FirebaseDatabase.getInstance().getReference("Doubt").child(uid)
        list.clear()
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (snap in snapshot.children){
                         val doubt=snap.getValue(DoubtModel::class.java)
                        if (doubt != null) {
                            Log.d("DoubtActivity", "Doubt fetched: $doubt")
                            list.add(doubt)
                        }
                    }
// Sort by timestamp in descending order
                    list.sortByDescending { it.timestamp }
                    if (list.isEmpty()) {
                        binding.showEmpty.visibility = View.VISIBLE // Show empty state
                        adapter.submitList(list)
                        adapter.notifyDataSetChanged()
                    } else {
                        binding.doubtRecyclerView.visibility=View.VISIBLE
                        binding.showEmpty.visibility = View.GONE    // Hide empty state
                        binding.doubtRecyclerView.adapter = adapter
                        adapter.submitList(list)
                        adapter.notifyDataSetChanged()
                    }


                }else{
                    binding.showEmpty.visibility= View.VISIBLE
                    adapter.submitList(list)
                    adapter.notifyDataSetChanged()
                }
                    binding.progressbar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progress.ToastShow(this@DoubtActivity, error.message)
                binding.showEmpty.visibility= View.VISIBLE
                binding.progressbar.visibility = View.GONE
            }

        })
    }

    private fun doubtAddDailog(doubtItem:DoubtModel?=null) {
        val dialog= Dialog(this)
        dialog.setContentView(R.layout.doubt_item_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.show()

        studImgView=dialog.findViewById<ImageView>(R.id.studUploadDoubtPhoto)
        val studDoubtQuesEditText=dialog.findViewById<TextView>(R.id.doubtEditTitle)
        val studDoubtDescEditText=dialog.findViewById<TextView>(R.id.doubtEditDesc)
        val doubtSubmitBtn=dialog.findViewById<TextView>(R.id.doubtSubmit)
        val doubtCancelBtn=dialog.findViewById<TextView>(R.id.doubtCancel)

        studImgView.setOnClickListener {
            selectImage.launch("image/*")
        }

        if(doubtItem!=null){
            studDoubtDescEditText.setText(doubtItem.studQuesDesc)
            studDoubtQuesEditText.setText(doubtItem.studQuesTitle)
            doubtSubmitBtn.setText("Update")
            Glide.with(this)
                .load(doubtItem.studQuesImgUrl)
                .placeholder(R.drawable.pyq_icon)
                .into(studImgView)
            imgUri=doubtItem.studQuesImgUrl?.toUri()
        }

        doubtSubmitBtn.setOnClickListener {
              val studQuesTitle=studDoubtQuesEditText.text.toString()
              val studQuesDesc=studDoubtDescEditText.text.toString()

              if(studQuesTitle.isNotEmpty() && studQuesDesc.isNotEmpty() ){
                     if(imgUri!=null){
                         if(doubtItem!=null){
                             updateDoubtItem(doubtItem,studQuesTitle,studQuesDesc)
                             dialog.dismiss()
                         }else{
                             uploadImg(studQuesTitle,studQuesDesc)
                             dialog.dismiss()
                         }
                     }else{
                         Toast.makeText(this,"Choose the Image",Toast.LENGTH_SHORT).show()
                     }
              }else{
              Toast.makeText(this,"Please Fill all the Detail",Toast.LENGTH_SHORT).show()
              }
        }

        doubtCancelBtn.setOnClickListener {
            dialog.dismiss()
        }


    }

    private fun uploadImg(studQuesTitle: String, studQuesDesc: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        if (imgUri == null) {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            return
        }
        progress.show(this)
        lifecycleScope.launch {
            try {
                val ref = FirebaseStorage.getInstance().getReference("Doubt/$currentUser/${UUID.randomUUID()}.jpg")
                val uploadTask = ref.putFile(imgUri!!)
                uploadTask.await()
                val downloadUrl = ref.downloadUrl.await()
                uploadDoubt(studQuesTitle, studQuesDesc, downloadUrl)
            } catch (e: Exception) {
                Toast.makeText(this@DoubtActivity, "Failed to upload: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progress.hide()
            }
        }
    }

    private fun uploadDoubt(studQuesTitle: String, studQuesDesc: String, downloadUrl: Uri?) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
         val reference=FirebaseDatabase.getInstance().getReference("Users").child(uid)
        reference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                 val user=snapshot.getValue(userDetail::class.java)
                 val name=user?.Name
                 val email=user?.Email
                 val imgUrl=user?.ProUrl
                val doubtId=idGenerate()

                val doubt=DoubtModel(
                    doubtId,
                    uid,
                    name,
                    imgUrl,
                    email,
                    downloadUrl.toString(),
                    studQuesTitle,
                    studQuesDesc,
                    "",
                    "",
                    timestamp = System.currentTimeMillis()
                )

                val ref=FirebaseDatabase.getInstance().getReference("Doubt").child(uid).child(doubtId)
                ref.setValue(doubt).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progress.ToastShow(this@DoubtActivity, "Upload Successful")
                        fetchData()
                        sendDoubtToTeacherNotification(studQuesTitle,  name!!)
                    } else {
                        progress.ToastShow(this@DoubtActivity, "Upload Failed")
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                progress.ToastShow(this@DoubtActivity, error.message)
            }
        })
    }
    private fun updateDoubtItem(item: DoubtModel, studQuesTitle: String, studQuesDesc: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val reference = FirebaseDatabase.getInstance().getReference("Doubt").child(uid).child(item.doubtId!!)

        // Fetch user details from "Users" node
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(userDetail::class.java)
                val name = user?.Name
                val email = user?.Email
                val imgUrl = user?.ProUrl

                // Create updated DoubtModel with the existing `doubtId`
                val doubt = DoubtModel(
                    item.doubtId, // Reuse existing doubtId
                    uid,
                    name,
                    imgUrl,
                    email,
                    imgUri?.toString() ?: item.studQuesImgUrl, // Use new image URI if provided, else retain old one
                    studQuesTitle,
                    studQuesDesc,
                    item.teachAnsImgUrl,
                    item.teachAnsDesc,
                    timestamp = System.currentTimeMillis() ,
                    null,
                    false
                )

                // Update the existing doubt
                reference.setValue(doubt).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progress.ToastShow(this@DoubtActivity, "Doubt updated successfully!")
                        fetchData() // Refresh the list
                        sendDoubtToTeacherNotification(studQuesTitle,   name!!)
                    } else {
                        progress.ToastShow(this@DoubtActivity, "Failed to update doubt.")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progress.ToastShow(this@DoubtActivity, "Error: ${error.message}")
            }
        })
    }
    fun idGenerate():String{
        return UUID.randomUUID().toString()
    }

    fun deleteDoubtFromFirebase(doubtId: String) {
        val uid = FirebaseAuth.getInstance().uid.orEmpty()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Doubt/$uid/$doubtId")
        AlertDialog.Builder(this)
            .setTitle("Delete Doubt")
            .setMessage("Are you sure you want to delete this doubt?")
            .setPositiveButton("Yes") { _, _ ->
                databaseReference.removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Doubt deleted successfully", Toast.LENGTH_SHORT).show()
                        fetchData() // Refresh the list
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { error ->
                        Toast.makeText(this, "Failed to delete: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("No", null)
            .show()
    }


    private fun filter(query: String?) {
        if (query != null && query.isNotEmpty()) {
            val filteredList = ArrayList<DoubtModel>()

            // Loop through the list and add items that contain the query (case-insensitive)
            for (item in list) {
                if (item.studQuesTitle?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true) {
                    filteredList.add(item)
                }
            }

            // Show a toast if no data matches the query
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            } else {
                // Submit the filtered list to the adapter
                adapter.submitList(filteredList)
            }
        } else {
            // If the query is null or empty, reset the adapter to the original list
            adapter.submitList(list)
        }
    }

    fun sendDoubtToTeacherNotification(doubtTitle: String, studentName: String) {
        val notificationTitle = "📝 New Doubt Submitted!"
        val notificationMessage = "🔍 $studentName has submitted a doubt: \"$doubtTitle\". Check it out and help resolve it! ✅"

        lifecycleScope.launch {
            FCMNotificationSender.sendNotification(
                title = notificationTitle,
                message = notificationMessage,
                topic = "doubtReceive"
            )
        }
    }


}
package com.example.nafisquaisarcoachingcenter.fragment

import android.R.attr.finishOnCloseSystemDialogs
import android.R.attr.name
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nafisquaisarcoachingcenter.FrontVIew
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.FragmentProfileBinding
import com.example.nafisquaisarcoachingcenter.model.userDetail
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.log


class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var databaseRefrence:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater, container, false)
        auth=FirebaseAuth.getInstance()

        binding.logoutButton.setOnClickListener(View.OnClickListener {
            signout()
            Toast.makeText(context,"LogOut Successfully",Toast.LENGTH_SHORT).show()
        })

//*********************************  Profle Update ALl Detail Dialog Box start **********************************************************
        databaseRefrence=FirebaseDatabase.getInstance().reference

        binding.profileEditButton.setOnClickListener{
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.edit_all_detail)
            dialog.show()
            var editTextupdateName= dialog.findViewById<EditText>(R.id.Update_name)
            var editTextupdateNumber=dialog.findViewById<EditText>(R.id.Update_Number)
            var editTextupdateCourse=dialog.findViewById<EditText>(R.id.Course_update)
            val updateButton=dialog.findViewById<Button>(R.id.final_profile_update_button)
            val canelButton=dialog.findViewById<Button>(R.id.final_profile_cancel_button)




            updateButton.setOnClickListener{

                val updateName=editTextupdateName.text.toString()
                val updateNumber=editTextupdateNumber.text.toString()
                val updateCourse=editTextupdateCourse.text.toString()


                if (updateName.isEmpty() || updateNumber.isEmpty() || updateCourse.isEmpty()){
                    Toast.makeText(context,"Please Fill all detail",Toast.LENGTH_SHORT).show()
                }else{
                    val currentUser=auth.currentUser
                    currentUser?.let {user->
                        val detail=databaseRefrence.child("users").child(user.uid).child("UserDetail").push().key


                        val userDetail=userDetail(updateName,updateNumber,updateCourse)
                        if(detail!=null){
                            databaseRefrence.child("users").child(user.uid).child(detail).setValue(userDetail)
                                .addOnCompleteListener{task->
                                    if (task.isSuccessful){
                                        Toast.makeText(context,"Profile Update Successfully",Toast.LENGTH_LONG).show()
                                        dialog.hide()
                                    }else{
                                        Toast.makeText(context,"Fail to Update Profile",Toast.LENGTH_SHORT).show()
                                    }

                                }
                        }
                    }
//                    binding.profileNumber.setText(updateNumber)
//                    binding.profileId.setText(updateCourse)
//                    binding.profileName.setText(updateName)
                }

            }

            canelButton.setOnClickListener{
                dialog.hide()
            }

        }

//*********************************  Profle Update ALl Detail Dialog Box End **********************************************************




        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            val personName = acct.displayName
            val personEmail = acct.email
            val personid=acct.id
            binding.profileEmail.setText(personEmail)
            binding.profileId.setText(personid)
            binding.profileName.setText(personName)
        }

        if (auth.getCurrentUser() != null) {
//            val number= auth.currentUser?.phoneNumber
            val personEmail = auth.currentUser?.email
//            val personid=auth.currentUser?.tenantId
            binding.profileEmail.setText(personEmail)
//            binding.profileId.setText(personid)
//            binding.profileNumber.setText(number)
        }



        return binding.root
    }


    fun signout(){
       //  ******************** Logout through using Google Start ********************************
        googleSignInClient.signOut().addOnCompleteListener(OnCompleteListener<Void?> {
            activity?.finish()
            startActivity(Intent(context, FrontVIew::class.java))
        })
       //   ******************** Logout through using Google End ********************************

        //        ******************** Logout through using Email and password Start ********************************
        if (auth.getCurrentUser() != null) {
            auth.signOut()
            val intent = Intent(context, FrontVIew::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
        //        ******************** Logout through using Email and password End ********************************
    }


    private fun getUserData(){
        databaseRefrence=FirebaseDatabase.getInstance().reference

    }



}
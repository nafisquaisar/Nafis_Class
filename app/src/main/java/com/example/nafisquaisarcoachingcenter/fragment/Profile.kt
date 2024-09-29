package com.example.nafisquaisarcoachingcenter.fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.nafisquaisarcoachingcenter.FrontVIew
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.FragmentProfileBinding
import com.example.nafisquaisarcoachingcenter.model.userDetail
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var databaseRefrence:DatabaseReference
    private var imgUri: Uri?=null




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

        binding.profileEditButton.setOnClickListener{
            updateProfileDialogBox()
        }

//*********************************  Profle Update ALl Detail Dialog Box End **********************************************************




       profileSet()


        return binding.root
    }

    private fun profileSet() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            val personName = acct.displayName
            val personEmail = acct.email
            val personid=acct.id

            binding.profileEmail.setText(personEmail)

            // List of titles to check for
            val titles = listOf("MD", "Mr.", "Dr.", "Ms.", "Mrs.", "Prof.", "Sr.", "Jr.")

            if (personName?.length ?: 0 > 13) {
                val nameParts = personName?.split(" ") ?: listOf("")
                var firstName = ""

                if (nameParts.size >= 3) {
                    // Combine the first two parts and check their length
                    val combinedFirstTwo = "${nameParts[0]} ${nameParts[1]}"
                    if (combinedFirstTwo.length <= 13) {
                        firstName = combinedFirstTwo // Use first two parts
                    } else {
                        // Handle the title case
                        if (titles.contains(nameParts[0])) {
                            firstName = nameParts.getOrElse(1) { "" } // Get the second part if it exists
                        } else {
                            firstName = nameParts.firstOrNull() ?: "" // Use the first part if no title
                        }
                    }
                } else if (nameParts.size == 2) {
                    // If only two parts, use both as long as they are <= 13
                    firstName = nameParts.joinToString(" ")
                } else {
                    firstName = nameParts.firstOrNull() ?: "" // Use the first part if only one part
                }

                binding.personName.setText(firstName) // Set the extracted firstName
            } else {
                binding.personName.setText(personName) // Set the full name if it's short
            }

            // Get the profile image URL
            val profileImageUrl = acct.photoUrl

            // Load the profile image into an ImageView using Glide
            if (profileImageUrl != null) {
                Glide.with(requireContext())
                    .load(profileImageUrl)
                    .placeholder(R.drawable.profile) // Optional placeholder image
                    .into(binding.profilePhoto) // Assuming you have an ImageView in your layout
            } else {
                // Handle the case where there is no profile image
                binding.profilePhoto.setImageResource(R.drawable.profile) // Set a default image
            }
        }
    }

    private fun updateProfileDialogBox() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.edit_all_detail)
        dialog.show()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        var email = acct?.email ?: ""
        var Name = acct?.displayName ?: ""

        databaseRefrence = FirebaseDatabase.getInstance().getReference("Users")


        var img = dialog.findViewById<ImageView>(R.id.profile_image)
        var selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            imgUri = it
            img.setImageURI(imgUri)
        }
        img.setOnClickListener {
            selectImage.launch("image/*")
        }

        var editTextupdateName = dialog.findViewById<EditText>(R.id.Update_name)
        var editTextupdateNumber = dialog.findViewById<EditText>(R.id.Number_update)
        var editTextShowEmail = dialog.findViewById<EditText>(R.id.showEmail)
        var editTextupdateClass = dialog.findViewById<EditText>(R.id.Course_update)
        val updateButton = dialog.findViewById<Button>(R.id.final_profile_update_button)
        val cancelButton = dialog.findViewById<Button>(R.id.final_profile_cancel_button)

        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val myRef = databaseRefrence.child(uid)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(userDetail::class.java)
                value?.let {
                    editTextupdateName.setText(it.Name)
                    editTextupdateNumber.setText(it.Number)
                    editTextupdateClass.setText(it.course)
                    Glide.with(requireContext())
                        .load(it.ProUrl)
                        .into(img)
                }
                editTextShowEmail.setText(email)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error loading data", Toast.LENGTH_SHORT).show()
            }
        })

        updateButton.setOnClickListener {
            val updateName = editTextupdateName.text.toString()
            val updateNumber = editTextupdateNumber.text.toString()
            val updateClass = editTextupdateClass.text.toString()

            if (updateName.isEmpty() || updateNumber.isEmpty() || updateClass.isEmpty()) {
                Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser
                currentUser?.let { user ->
                    if (imgUri != null) {
                        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/${uid}")
                        storageRef.putFile(imgUri!!).addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()
                                val userDetail = userDetail(user.uid, updateName, email, updateNumber, updateClass, imageUrl)
                                databaseRefrence.child("users").child(user.uid).child("UserDetail").setValue(userDetail)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_LONG).show()
                                            dialog.hide()
                                        } else {
                                            Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        }.addOnFailureListener {
                            Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        cancelButton.setOnClickListener {
            dialog.hide()
        }
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
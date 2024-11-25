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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.nafisquaisarcoachingcenter.DoubtActivity
import com.example.nafisquaisarcoachingcenter.FrontVIew
import com.example.nafisquaisarcoachingcenter.HelpAndCare
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.FragmentProfileBinding
import com.example.nafisquaisarcoachingcenter.model.userDetail
import com.example.nafisquaisarcoachingcenter.progress
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var databaseReference: DatabaseReference
    private var imgUri: Uri? = null
    private lateinit var imgDialog: ImageView

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imgUri = uri
        imgDialog.setImageURI(imgUri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        setupGoogleSignIn()
        profileSet()

        binding.logoutButton.setOnClickListener {
            signOut()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        }

        binding.profileEditButton.setOnClickListener {
            updateProfileDialogBox()
        }

        binding.llHelpcare.setOnClickListener {
            val intent=Intent(requireContext(),HelpAndCare::class.java)
            startActivity(intent)
        }

        binding.llMydoubt.setOnClickListener {
            startActivity(Intent(requireContext(),DoubtActivity::class.java))
        }

        return binding.root
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun profileSet() {
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        val email = account?.email ?: ""
        val googleDisplayName = account?.displayName ?: ""
        val googleProfileUrl = account?.photoUrl?.toString() ?: ""

        // Check if fragment is attached before performing UI operations
        if (isAdded) {
            binding.personName.setText(shortenName(googleDisplayName))
            binding.profileEmail.setText(email)

            Glide.with(requireContext())
                .load(googleProfileUrl)
                .placeholder(R.drawable.user_profile)
                .into(binding.profilePhoto)
        }

        val uid = auth.currentUser?.uid ?: return
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(userDetail::class.java)
                user?.let {
                    // Check if fragment is still attached before updating UI
                    if (isAdded) {
                        binding.personName.setText(it.Name.ifEmpty { googleDisplayName })
                        binding.profileEmail.setText(it.Email.ifEmpty { email })

                        Glide.with(requireContext())
                            .load(it.ProUrl.ifEmpty { googleProfileUrl })
                            .placeholder(R.drawable.user_profile_icon)
                            .into(binding.profilePhoto)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Check if fragment is still attached before showing a toast
                if (isAdded) {
                    Toast.makeText(requireContext(), "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updateProfileDialogBox() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.edit_all_detail)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.show()

        imgDialog = dialog.findViewById(R.id.profile_image)
        val editButton = dialog.findViewById<FloatingActionButton>(R.id.edit_button)
        val updateName = dialog.findViewById<EditText>(R.id.Update_name)
        val updateNumber = dialog.findViewById<EditText>(R.id.Number_update)
        val updateEmail=dialog.findViewById<EditText>(R.id.showEmail)
        val updateClass = dialog.findViewById<EditText>(R.id.Course_update)
        val updateButton = dialog.findViewById<Button>(R.id.final_profile_update_button)
        val cancelButton = dialog.findViewById<Button>(R.id.final_profile_cancel_button)

        val uid = auth.currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(userDetail::class.java)
                user?.let {
                    updateName.setText(it.Name)
                    updateNumber.setText(it.Number)
                    updateClass.setText(it.course)
                    updateEmail.setText(it.Email)
                    Glide.with(requireContext())
                        .load(it.ProUrl)
                        .placeholder(R.drawable.user_profile_icon)
                        .into(imgDialog)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })

        editButton.setOnClickListener {
            selectImage.launch("image/*")
        }

        updateButton.setOnClickListener {
            val name = updateName.text.toString()
            val number = updateNumber.text.toString()
            val course = updateClass.text.toString()

            if (name.isEmpty() || number.isEmpty() || course.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imgUri != null) {
                uploadImageAndUpdate(name, number, course)
            } else {
                userRef.child("ProUrl").get().addOnSuccessListener {
                    val existingUrl = it.value.toString()
                    updateData(name, number, course, Uri.parse(existingUrl))
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Error fetching image URL", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }

        cancelButton.setOnClickListener { dialog.dismiss() }
    }

    private fun uploadImageAndUpdate(name: String, number: String, course: String) {
        val currentUserUid = auth.currentUser?.uid ?: return
        val storageRef = FirebaseStorage.getInstance().getReference("UserProfile").child(currentUserUid).child("Profile.jpg")

        lifecycleScope.launch {
            try {
                val uploadTask = storageRef.putFile(imgUri!!).await()
                if (uploadTask.task.isSuccessful) {
                    val downloadUrl = storageRef.downloadUrl.await()
                    updateData(name, number, course, downloadUrl)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateData(name: String, number: String, course: String, profileUrl: Uri?) {
        val uid = auth.currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        val updatedUser = userDetail(uid, name, auth.currentUser?.email ?: "", number, "", course, profileUrl.toString())

        userRef.setValue(updatedUser).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener {
            startActivity(Intent(requireContext(), FrontVIew::class.java))
            requireActivity().finish()
        }
        auth.signOut()
    }

    private fun shortenName(name: String): String {
        return if (name.length > 13) name.split(" ").take(2).joinToString(" ") else name
    }
}

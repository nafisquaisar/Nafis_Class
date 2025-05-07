package com.nafis.organizerclasses.fragment

import EmailSender
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
import com.nafis.organizerclasses.AppSettingActivity
import com.nafis.organizerclasses.DoubtActivity
import com.nafis.organizerclasses.FeedBack
import com.nafis.organizerclasses.FrontVIew
import com.nafis.organizerclasses.HelpAndCare
import com.nafis.organizerclasses.PrivacyPolicy
import com.nafis.organizerclasses.RefundPolicy
import com.nafis.organizerclasses.databinding.FragmentProfileBinding
import com.nafis.organizerclasses.model.userDetail
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.nafis.organizerclasses.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

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
        binding.llSetting.setOnClickListener {
            startActivity(Intent(requireContext(),AppSettingActivity::class.java))
        }
        binding.llFeedback.setOnClickListener {
            startActivity(Intent(requireContext(),FeedBack::class.java))
        }
        binding.llPrivacyPolicy.setOnClickListener {
            startActivity(Intent(requireContext(),PrivacyPolicy::class.java))
        }
        binding.llRefundCancellationPolicy.setOnClickListener {
            startActivity(Intent(requireContext(),RefundPolicy::class.java))
        }
        binding.llShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Check out this amazing app!")
                putExtra(Intent.EXTRA_TEXT, "Hey! Check out this app: https://play.google.com/store/apps/details?id=com.nafis.organizerclasses")
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        binding.llRating.setOnClickListener {
            try {
                val uri = Uri.parse("market://details?id=com.nafis.organizerclasses")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.android.vending") // Opens Play Store directly
                startActivity(intent)
            } catch (e: Exception) {
                // Fallback to browser if Play Store is not available
                val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.nafis.organizerclasses")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }

        return binding.root
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun profileSet() {
        try {
            val account = GoogleSignIn.getLastSignedInAccount(requireContext())
            val email = account?.email ?: ""
            val googleDisplayName = account?.displayName ?: ""
            val googleProfileUrl = account?.photoUrl?.toString() ?: ""

            if (isAdded) {
                binding.personName.text = shortenName(googleDisplayName)
                binding.profileEmail.text = email

                Glide.with(requireContext())
                    .load(googleProfileUrl)
                    .placeholder(R.drawable.user_profile_icon)
                    .into(binding.profilePhoto)
            }

            val uid = auth.currentUser?.uid ?: return
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid)

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val user = snapshot.getValue(userDetail::class.java)
                        if (isAdded && user != null) {
                            binding.personName.text = user.Name.ifEmpty { googleDisplayName }
                            binding.profileEmail.text = user.Email.ifEmpty { email }

                            Glide.with(requireContext())
                                .load(user.ProUrl.ifEmpty { googleProfileUrl })
                                .placeholder(R.drawable.user_profile_icon)
                                .into(binding.profilePhoto)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Data Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (isAdded) {
                        Toast.makeText(requireContext(), "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error fetching profile data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProfileDialogBox() {
        try {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.edit_all_detail)
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.show()

            try {
                // Initialize views
                imgDialog = dialog.findViewById(R.id.profile_image)
                val editButton = dialog.findViewById<FloatingActionButton>(R.id.edit_button)
                val updateName = dialog.findViewById<EditText>(R.id.Update_name)
                val updateNumber = dialog.findViewById<EditText>(R.id.Number_update)
                val updateEmail = dialog.findViewById<EditText>(R.id.showEmail)
                val updateClass = dialog.findViewById<EditText>(R.id.Course_update)
                val updateButton = dialog.findViewById<Button>(R.id.final_profile_update_button)
                val cancelButton = dialog.findViewById<Button>(R.id.final_profile_cancel_button)

                val uid = auth.currentUser?.uid ?: return
                val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)

                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            val user = snapshot.getValue(userDetail::class.java)

                            // Fetch Google account details
                            val account = GoogleSignIn.getLastSignedInAccount(requireContext())
                            val googleEmail = account?.email ?: ""
                            val googleDisplayName = account?.displayName ?: ""
                            val googleProfileUrl = account?.photoUrl?.toString() ?: ""

                            // Populate fields from database or use Google account details as fallback
                            val name = user?.Name.takeIf { !it.isNullOrEmpty() } ?: googleDisplayName
                            val number = user?.Number.takeIf { !it.isNullOrEmpty() } ?: ""
                            val clas = user?.course.takeIf { !it.isNullOrEmpty() } ?: ""
                            val email = user?.Email.takeIf { !it.isNullOrEmpty() } ?: googleEmail

                            // Prioritize the database profile URL if it exists and is not empty
                            val profileUrl = user?.ProUrl.takeIf { !it.isNullOrEmpty() } ?: googleProfileUrl

                            updateName.setText(name)
                            updateNumber.setText(number)
                            updateClass.setText(clas)
                            updateEmail.setText(email)

                            Glide.with(requireContext())
                                .load(profileUrl)
                                .placeholder(R.drawable.user_profile_icon)
                                .error(R.drawable.user_profile_icon)
                                .into(imgDialog)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(requireContext(), "Error loading profile data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                    }
                })

                // Handle image selection
                editButton.setOnClickListener {
                    try {
                        selectImage.launch("image/*")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Error selecting image", Toast.LENGTH_SHORT).show()
                    }
                }

                // Handle profile updates
                updateButton.setOnClickListener {
                    try {
                        val name = updateName.text.toString()
                        val number = updateNumber.text.toString()
                        val course = updateClass.text.toString()

                        if (name.isEmpty() || number.isEmpty()) {
                            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        if (imgUri != null) {
                            uploadImageAndUpdate(name, number, course)
                        } else {
                            userRef.child("ProUrl").get().addOnSuccessListener {
                                try {
                                    val existingUrl = it.value.toString()
                                    updateData(name, number, course, Uri.parse(existingUrl))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Toast.makeText(requireContext(), "Error updating profile", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Toast.makeText(requireContext(), "Error fetching image URL", Toast.LENGTH_SHORT).show()
                            }
                        }

                        dialog.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Error updating profile", Toast.LENGTH_SHORT).show()
                    }
                }

                // Handle cancel button
                cancelButton.setOnClickListener { dialog.dismiss() }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error initializing dialog", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Unexpected error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageAndUpdate(name: String, number: String, course: String) {
        try {
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
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Unexpected error in image upload", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData(name: String, number: String, course: String, profileUrl: Uri?) {
        val uid = auth.currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        val updatedUser = userDetail(uid, name, auth.currentUser?.email ?: "", number, "", course, profileUrl.toString())

        userRef.setValue(updatedUser).addOnCompleteListener {
            if (it.isSuccessful) {
                profileSet()
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

package com.nafis.organizerclasses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.nafis.organizerclasses.databinding.ActivityUsingNumberLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class UsingNumberLogin : AppCompatActivity() {
    private lateinit var sendOTPBtn : Button
    private lateinit var phoneNumberET : EditText
    private lateinit var auth : FirebaseAuth
    private lateinit var number : String
    private lateinit var mProgressBar : ProgressBar
    private lateinit var binding:ActivityUsingNumberLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUsingNumberLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        init()
        sendOTPBtn.setOnClickListener {
            number = phoneNumberET.text.trim().toString()
            if (number.isNotEmpty()){
                if (number.length == 10){
                    number = "+91$number"
                    mProgressBar.visibility = View.VISIBLE
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)

                }else{
                    Toast.makeText(this , "Please Enter correct Number" , Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this , "Please Enter Number" , Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun init(){
        mProgressBar = binding.phoneProgressBar
        mProgressBar.visibility = View.INVISIBLE
        sendOTPBtn = binding.sendOTPBtn
        phoneNumberET =binding.phoneEditTextNumber
        auth = FirebaseAuth.getInstance()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this , "Authenticate Successfully" , Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
                mProgressBar.visibility = View.INVISIBLE
            }
    }

    private fun checkUserAndProceed() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid // Retrieve the UID of the currently logged-in user
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)

            userRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.exists()) {
                        // User exists in database, redirect to Main Dashboard
                        sendToMain()
                    } else {
                        // User does not exist, show pop-up to collect details
                        showDetailsPopup(uid)
                    }
                } else {
                    // Handle errors
                    Log.e("FirebaseError", "Error fetching user data: ${task.exception?.message}")
                }
            }
        } else {
            Log.e("AuthError", "User not logged in")
        }
    }

    private fun sendToMain() {
        startActivity(Intent(this, Main_dashboard::class.java))
        finish()
    }

    private fun showDetailsPopup(uid: String) {
        // Inflate and configure the pop-up layout
        val dialogView = layoutInflater.inflate(R.layout.add_data_using_phone_otp, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val nameEditText = dialogView.findViewById<EditText>(R.id.Update_name)
        val emailEditText = dialogView.findViewById<EditText>(R.id.showEmail)
        val okButton = dialogView.findViewById<AppCompatButton>(R.id.final_profile_update_button)
        val cancelButton = dialogView.findViewById<AppCompatButton>(R.id.final_profile_cancel_button)

        cancelButton.setOnClickListener { alertDialog.dismiss() }
        okButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (name.isNotEmpty()) {
                saveUserDetails(uid, name,email)
                alertDialog.dismiss()
                sendToMain()
            } else {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }

    private fun saveUserDetails(uid: String, name: String, email: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        val userDetails = mapOf(
            "id" to uid,
            "Name" to name,
            "Email" to email
        )
        userRef.setValue(userDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseError", "Error saving user details: ${e.message}")
            }
    }

    private fun currentUserEmail(): String {
        return FirebaseAuth.getInstance().currentUser?.email ?: "Unknown Email"
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.e("FirebaseAuth", "Invalid request: ${e.localizedMessage}")
                Toast.makeText(this@UsingNumberLogin, "Invalid phone number format.", Toast.LENGTH_SHORT).show()
            } else if (e is FirebaseTooManyRequestsException) {
                // SMS quota exceeded
                Log.e("FirebaseAuth", "SMS quota exceeded: ${e.localizedMessage}")
                Toast.makeText(this@UsingNumberLogin, "SMS quota exceeded. Try again later.", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("FirebaseAuth", "Verification failed: ${e.localizedMessage}")
                Toast.makeText(this@UsingNumberLogin, "Failed to verify. Try again later.", Toast.LENGTH_SHORT).show()
            }
            mProgressBar.visibility = View.INVISIBLE
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later
            val intent = Intent(this@UsingNumberLogin, OtpActivity::class.java)
            intent.putExtra("OTP" , verificationId)
            intent.putExtra("resendToken" , token)
            intent.putExtra("phoneNumber" , number)
            startActivity(intent)
            mProgressBar.visibility = View.INVISIBLE
        }
    }


    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            startActivity(Intent(this , Main_dashboard::class.java))
        }
    }
}
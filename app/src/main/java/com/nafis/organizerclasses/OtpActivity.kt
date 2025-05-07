package com.nafis.organizerclasses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.nafis.organizerclasses.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOtpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var verifyBtn: Button
    private lateinit var resendTV: TextView
    private lateinit var inputOTP1: EditText
    private lateinit var inputOTP2: EditText
    private lateinit var inputOTP3: EditText
    private lateinit var inputOTP4: EditText
    private lateinit var inputOTP5: EditText
    private lateinit var inputOTP6: EditText
    private lateinit var progressBar: ProgressBar

    private lateinit var OTP: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOtpBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        OTP = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("phoneNumber")!!

        init()
        progressBar.visibility = View.INVISIBLE
        addTextChangeListener()
        resendOTPTvVisibility()

        resendTV.setOnClickListener {
            resendVerificationCode()
            resendOTPTvVisibility()
        }

        verifyBtn.setOnClickListener {
            //collect otp from all the edit texts
            val typedOTP =
                (inputOTP1.text.toString() + inputOTP2.text.toString() + inputOTP3.text.toString()
                        + inputOTP4.text.toString() + inputOTP5.text.toString() + inputOTP6.text.toString())

            if (typedOTP.isNotEmpty()) {
                if (typedOTP.length == 6) {
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP, typedOTP
                    )
                    progressBar.visibility = View.VISIBLE
                    signInWithPhoneAuthCredential(credential)
                } else {
                    Toast.makeText(this, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun resendOTPTvVisibility() {
        inputOTP1.setText("")
        inputOTP2.setText("")
        inputOTP3.setText("")
        inputOTP4.setText("")
        inputOTP5.setText("")
        inputOTP6.setText("")
        resendTV.visibility = View.INVISIBLE
        resendTV.isEnabled = false

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            resendTV.visibility = View.VISIBLE
            resendTV.isEnabled = true
        }, 60000)
    }

    private fun resendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            }
            progressBar.visibility = View.INVISIBLE
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later
            OTP = verificationId
            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                    checkUserAndProceed()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
                progressBar.visibility = View.VISIBLE
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
        val dialogView = layoutInflater.inflate(com.nafis.organizerclasses.R.layout.add_data_using_phone_otp, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val nameEditText = dialogView.findViewById<EditText>(com.nafis.organizerclasses.R.id.Update_name)
        val emailEditText = dialogView.findViewById<EditText>(com.nafis.organizerclasses.R.id.showEmail)
        val okButton = dialogView.findViewById<AppCompatButton>(com.nafis.organizerclasses.R.id.final_profile_update_button)
        val cancelButton = dialogView.findViewById<AppCompatButton>(com.nafis.organizerclasses.R.id.final_profile_cancel_button)

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
            "Email" to email,
            "Number" to phoneNumber
        )
        userRef.setValue(userDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseError", "Error saving user details: ${e.message}")
            }
    }


    private fun addTextChangeListener() {
        inputOTP1.addTextChangedListener(EditTextWatcher(inputOTP1))
        inputOTP2.addTextChangedListener(EditTextWatcher(inputOTP2))
        inputOTP3.addTextChangedListener(EditTextWatcher(inputOTP3))
        inputOTP4.addTextChangedListener(EditTextWatcher(inputOTP4))
        inputOTP5.addTextChangedListener(EditTextWatcher(inputOTP5))
        inputOTP6.addTextChangedListener(EditTextWatcher(inputOTP6))
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        progressBar =  binding.otpProgressBar
        verifyBtn = binding.verifyOTPBtn
        resendTV =  binding.resendTextView
        inputOTP1 = binding.otpEditText1
        inputOTP2 = binding.otpEditText2
        inputOTP3 = binding.otpEditText3
        inputOTP4 = binding.otpEditText4
        inputOTP5 = binding.otpEditText5
        inputOTP6 = binding.otpEditText6
    }


    inner class EditTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No implementation needed
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // No implementation needed
        }

        override fun afterTextChanged(s: Editable?) {
            val text = s.toString()
            when (view.id) {
                binding.otpEditText1.id -> if (text.length == 1) binding.otpEditText2.requestFocus()
                binding.otpEditText2.id -> if (text.length == 1) binding.otpEditText3.requestFocus() else if (text.isEmpty()) binding.otpEditText1.requestFocus()
                binding.otpEditText3.id -> if (text.length == 1) binding.otpEditText4.requestFocus() else if (text.isEmpty()) binding.otpEditText2.requestFocus()
                binding.otpEditText4.id -> if (text.length == 1) binding.otpEditText5.requestFocus() else if (text.isEmpty()) binding.otpEditText3.requestFocus()
                binding.otpEditText5.id -> if (text.length == 1) binding.otpEditText6.requestFocus() else if (text.isEmpty()) binding.otpEditText4.requestFocus()
                binding.otpEditText6.id -> if (text.isEmpty()) binding.otpEditText5.requestFocus()
            }
        }
    }
}
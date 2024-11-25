package com.example.nafisquaisarcoachingcenter

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.nafisquaisarcoachingcenter.Internet.InternetNotAvailableActivity
import com.example.nafisquaisarcoachingcenter.databinding.ActivityLoginBinding
import com.example.nafisquaisarcoachingcenter.databinding.ForgotPassLayoutBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private var forgotDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()

        // Navigation to Sign Up page
        binding.LSignUpButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        // Login Button functionality
        binding.LoginButton.setOnClickListener {
            if (NetworkUtil.isInternetAvailable(this)) {
                login() // Call the login method when the button is clicked
            } else {
                Toast.makeText(
                    this,
                    "No internet connection. Please check your connection and try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Forgot password functionality
        binding.forgot.setOnClickListener {
            showForgotPasswordDialog()
        }

        // Set up Google Sign-In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Google Login button functionality
        binding.googleLoginButton.setOnClickListener {
            if (NetworkUtil.isInternetAvailable(this)) {
                dismissAllDialogs() // Dismiss dialogs before launching intent
                val signInIntent: Intent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, 1000)
            } else {
                Toast.makeText(
                    this,
                    "No internet connection. Please check your connection and try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun login() {
        progress.show(this)

        val email = binding.loginEmail.text.toString().trim()
        val password = binding.loginPassword.text.toString().trim()

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            dismissAllDialogs()
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty() || password.isEmpty()) {
            dismissAllDialogs()
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                dismissAllDialogs()
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user?.isEmailVerified == true) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Main_dashboard::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show()
                        auth.signOut()
                    }
                } else {
                    val errorMessage = when (val exception = task.exception) {
                        is FirebaseAuthInvalidUserException -> "No account found with this email. Please sign up first."
                        is FirebaseAuthInvalidCredentialsException -> "Incorrect password. Please try again."
                        else -> "Login failed: ${exception?.localizedMessage}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                dismissAllDialogs()
                Toast.makeText(this, "An error occurred: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showForgotPasswordDialog() {
        val dialogBinding = ForgotPassLayoutBinding.inflate(LayoutInflater.from(this))
        forgotDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        forgotDialog?.show()

        dialogBinding.forgotbtn.setOnClickListener {
            val email = dialogBinding.fogotemail.editText?.text.toString()
            if (email.isNotEmpty()) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                Toast.makeText(this, "Please Check your email", Toast.LENGTH_SHORT).show()
                dismissAllDialogs()
            } else {
                Toast.makeText(this, "Enter the Email", Toast.LENGTH_SHORT).show()
            }
        }

        dialogBinding.forgotcancel.setOnClickListener {
            dismissAllDialogs()
        }
    }



    private fun dismissAllDialogs() {
        forgotDialog?.dismiss()
        progress.hide()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    val idToken = account.idToken
                    if (idToken != null) {
                        firebaseAuthWithGoogle(idToken)
                    } else {
                        dismissAllDialogs()
                        Toast.makeText(applicationContext, "ID Token is null", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    dismissAllDialogs()
                    Toast.makeText(applicationContext, "Account is null", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                dismissAllDialogs()
                Toast.makeText(applicationContext, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                dismissAllDialogs()
                if (task.isSuccessful) {
                    startActivity(Intent(this, Main_dashboard::class.java))
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Firebase Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismissAllDialogs() // Dismiss dialogs when back is pressed
    }
}

package com.nafis.organizerclasses

import EmailSender
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.nafis.organizerclasses.databinding.ActivitySignUpBinding
import com.nafis.organizerclasses.databinding.SucceesLayoutBinding
import com.nafis.organizerclasses.databinding.WaitingBinding
import com.nafis.organizerclasses.model.userDetail
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SignUp : AppCompatActivity() {
    private val binding:ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // intialize auth firebase
        auth=FirebaseAuth.getInstance()

        binding.SignInButton.setOnClickListener {
            val intent=Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        binding.RegisterButton.setOnClickListener {
            signUpProcess()
        }


        val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (acct != null) {
            startActivity(Intent(this, Main_dashboard::class.java))
            finish()
        }
       // Replace with your actual web client ID from the Google Cloud Console
        val googleSignUpOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id)) // Request the ID token
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignUpOption)

        binding.googleSignUpButton.setOnClickListener {
            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 1000)
        }


    }

    private fun signUpProcess() {
        val email = binding.signupEmail.text.toString()
        val number = binding.signupMobile.text.toString()
        val name = binding.signupName.text.toString()
        val password = binding.SignupPassword.text.toString()
        val repassword = binding.signupRepassword.text.toString()

        if (email.isEmpty() || number.isEmpty() || password.isEmpty() || repassword.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please Enter all Detail", Toast.LENGTH_LONG).show()
        } else if (password != repassword) {
            Toast.makeText(this, "Repeat Password must be same", Toast.LENGTH_LONG).show()
        } else {
          saveUserData(name,email,number,password)
        }
    }

    private fun saveUserData(name: String, email: String, number: String, password: String) {
        val dialogBinding = WaitingBinding.inflate(LayoutInflater.from(this))
        val waitingDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()
        waitingDialog.show()

        val firebaseAuth = FirebaseAuth.getInstance()
        val db = FirebaseDatabase.getInstance().getReference("Users")

        lifecycleScope.launch {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                        // Email verification sent successfully
                        waitingDialog.dismiss()

                        // Save user data to Firebase Realtime Database
                        val uid = firebaseAuth.currentUser?.uid.toString()
                        val userDetail = userDetail(uid, name, email, number, password, "", "")
                        db.child(uid).setValue(userDetail)

                         sendSuccessEmail(name,email)


                        // Show success dialog and inform the user to verify email
                        showSuccessDialog()

                    }?.addOnFailureListener { e ->
                        waitingDialog.dismiss()
                        progress.ToastShow(this@SignUp,"Failed to send verification email: ${e.message}")
                    }
                } else {
                    waitingDialog.dismiss()
                    progress.ToastShow(this@SignUp,"Account creation failed: ${task.exception?.message}")
                }
            }
        }
    }

    private fun showSuccessDialog() {
        val successDialogBinding = SucceesLayoutBinding.inflate(LayoutInflater.from(this))
        val successDialog = AlertDialog.Builder(this)
            .setView(successDialogBinding.root)
            .create()
        successDialog.show()

        successDialogBinding.btnOKCreate.setOnClickListener {
            successDialog.dismiss()

            // Redirect to the login screen after the success dialog is closed
            startActivity(Intent(this, Login::class.java))
            finish()

            // Show a message asking them to verify their email
            Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    // Check if the idToken is not null before proceeding
                    val idToken = account.idToken
                    if (idToken != null) {
                        firebaseAuthWithGoogle(idToken)
                    } else {
                        Toast.makeText(applicationContext, "ID Token is null", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(applicationContext, "Account is null", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Authenticate with Firebase using Google ID token
// Authenticate with Firebase using Google ID token
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Successfully authenticated, now upload details
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null && currentUser.isEmailVerified) {
                        uploadDetail()
                        val intent = Intent(this@SignUp, Main_dashboard::class.java)
                        startActivity(intent)
                        // Send success email
                        Log.d("EmailDebug", "Calling sendSuccessEmail for ${currentUser.email}")

                        currentUser.displayName?.let { sendSuccessEmail(it, currentUser.email!!) }
                        Toast.makeText(this, "SignUp Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        // If email is not verified, prompt the user to verify
                        Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Firebase Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun uploadDetail() {
        progress.show(this)

        // Get the authenticated user's UID from Firebase Authentication
        val user = FirebaseAuth.getInstance().currentUser?.uid

        if (user != null) {
            // Get Google Sign-In account details
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            val gName = acct?.displayName ?: "Unknown"
            val gEmail = acct?.email ?: "No Email"
            val gProUrl = acct?.photoUrl ?: Uri.parse("default_url")

            // Save user details in Firebase Realtime Database
            val db = FirebaseDatabase.getInstance().getReference("Users").child(user)
            val userDetails = userDetail(user, gName, gEmail, "", "", "", gProUrl.toString())
            db.setValue(userDetails)
                .addOnSuccessListener {
                    progress.hide()
                    Toast.makeText(this@SignUp, "Account Created Successfully", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener {
                    progress.hide()
                    Toast.makeText(this@SignUp, "Account Creation Failed", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            progress.hide()
            Toast.makeText(this@SignUp, "User Not Found", Toast.LENGTH_SHORT).show()
        }
    }



    private fun sendSuccessEmail(name: String, email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val subjectText = "Welcome to Organizer Classes 🎉"
            val messageBody = """
        <html>
        <body>
            <h2 style="color: #2E86C1;">Dear $name,</h2>
            <p>Congratulations! 🎉 You have successfully registered on Organizer Classes.</p>
            <p>Your email: <b>$email</b></p>
            <p>Please verify your email before logging in.</p>
            <br>
            <p>Thank you for joining us!</p>
            <br>
            <p>Best Regards,<br><b>Organizer Classes Team</b></p>
        </body>
        </html>
        """.trimIndent()

            // **Create an instance of EmailSender**
            val sender = EmailSender("organizerclasses@gmail.com", "vxtu zkbx qmuw ayrn")

            // **Call sendEmail function from the instance**
            val isSent = sender.sendEmail(email, subjectText, messageBody)

            // **Switch back to the Main thread for UI updates**
            withContext(Dispatchers.Main) {
                if (isSent) {
                    Toast.makeText(this@SignUp, "Registration confirmation email sent.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SignUp, "Failed to send email.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}

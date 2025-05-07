package com.nafis.organizerclasses

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.nafis.organizerclasses.Internet.InternetNotAvailableActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FrontVIew : AppCompatActivity()   {

    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()

        if (NetworkUtil.isInternetAvailable(this)) {
            checkUserStatus()
        } else {
            // If there's no internet, start the InternetNotAvailableActivity
            startActivity(Intent(this, InternetNotAvailableActivity::class.java))
        }
    }

    private fun checkUserStatus() {
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            // User is logged in with Firebase, proceed to dashboard
            startActivity(Intent(this, Main_dashboard::class.java))
            finish() // Finish FrontVIew when user is logged in
        } else {
            val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
            if (acct != null) {
                // User is signed in with Google, proceed to dashboard
                startActivity(Intent(this, Main_dashboard::class.java))
                finish() // Finish FrontVIew when user is logged in
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front_view)

        val signUpButton: Button = findViewById(R.id.signup_home_button)
        val loginButton: Button = findViewById(R.id.login_home_button)
//        val loginUsingNumberButton: Button = findViewById(R.id.login_UsingNumeber_button)

        loginButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent) // Don't call finish() here
        }

        signUpButton.setOnClickListener {
            val spIntent = Intent(this, SignUp::class.java)
            startActivity(spIntent) // Don't call finish() here
        }
//        loginUsingNumberButton.setOnClickListener {
//            val spIntent = Intent(this, UsingNumberLogin::class.java)
//            startActivity(spIntent) // Don't call finish() here
//        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true) // Minimizes the app
    }
}

package com.example.nafisquaisarcoachingcenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FrontVIew : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth


    override fun onStart() {
        super.onStart()
        // check if user already logged in
        auth=FirebaseAuth.getInstance()
        val currentUser : FirebaseUser?=auth.currentUser
        if(currentUser!=null){
            startActivity(Intent(this,Main_dashboard::class.java))
            finish()
        }

        val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (acct != null) {
            startActivity(Intent(this, Main_dashboard::class.java))
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front_view)

        val signUpButton: Button =findViewById(R.id.signup_home_button)
        val loginbutton: Button =findViewById(R.id.login_home_button)



        loginbutton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener{
            val spintent= Intent(this,SignUp::class.java)
            startActivity(spintent)
        }

    }
}
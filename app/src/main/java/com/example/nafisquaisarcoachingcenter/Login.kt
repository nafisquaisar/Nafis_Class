package com.example.nafisquaisarcoachingcenter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nafisquaisarcoachingcenter.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Login : AppCompatActivity() {
    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        // check if user already logged in
        val currentUser : FirebaseUser?=auth.currentUser
        if(currentUser!=null){
            startActivity(Intent(this, Main_dashboard::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize firebase
        auth=FirebaseAuth.getInstance()


        binding.LSignUpButton.setOnClickListener {
            val intent=Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        binding.LoginButton.setOnClickListener{
            // fetching all detail from login activity
            val email=binding.loginEmail.text.toString()
            val password=binding.loginPassword.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Please Enter ALl Detail",Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener{task->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Login Successfully",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Main_dashboard::class.java))
                            finish()
                        }else{
                            Toast.makeText(this,"Login Fail ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }


        val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (acct != null) {
            startActivity(Intent(this, Main_dashboard::class.java))
            finish()
        }

        val gso =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)
        binding.googleLoginButton.setOnClickListener{
            val signInIntent: Intent = googleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, 1000)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                navigateToSecondActivity()

            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun navigateToSecondActivity() {
        val intent: Intent = Intent(this@Login, Main_dashboard::class.java)
        startActivity(intent)
        Toast.makeText(this,"Login Successfully",Toast.LENGTH_SHORT).show()

        finish()
    }
}
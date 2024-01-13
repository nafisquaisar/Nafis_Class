package com.example.nafisquaisarcoachingcenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nafisquaisarcoachingcenter.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {
    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

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

    }
}
package com.example.nafisquaisarcoachingcenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nafisquaisarcoachingcenter.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private val binding:ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
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
            // fetching all detial from users
            val email = binding.signupEmail.text.toString()
            val number = binding.signupMobile.text.toString()
            val password = binding.SignupPassword.text.toString()
            val repassword = binding.signupRepassword.text.toString()

            // check if any field is empty

            if (email.isEmpty() || number.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
                Toast.makeText(this, "Please Enter all Detail", Toast.LENGTH_LONG).show()
            } else if (password != repassword){
                Toast.makeText(this, "Repeat Password must be same", Toast.LENGTH_LONG).show()
           }else{
               // save detail in data base
               auth.createUserWithEmailAndPassword(email,password)
                   .addOnCompleteListener(this){task->
                       if(task.isSuccessful){
                           Toast.makeText(this,"Register Successful",Toast.LENGTH_SHORT).show()
                           startActivity(Intent(this, Login::class.java))
                       }else{
                           Toast.makeText(this,"Register Fail ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                       }
                   }
           }


         }

    }
}
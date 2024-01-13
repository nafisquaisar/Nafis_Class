package com.example.nafisquaisarcoachingcenter.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nafisquaisarcoachingcenter.FrontVIew
import com.example.nafisquaisarcoachingcenter.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater, container, false)
        auth=FirebaseAuth.getInstance()

        binding.logoutButton.setOnClickListener(View.OnClickListener {
           signout()
            Toast.makeText(context,"LogOut Successfully",Toast.LENGTH_SHORT).show()
        })


        return binding.root

    }


    fun signout(){
        if (auth.getCurrentUser() != null) {
            auth.signOut()
            val intent = Intent(context, FrontVIew::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
    }



}
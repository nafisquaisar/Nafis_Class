package com.example.nafisquaisarcoachingcenter.coursecclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.example.nafis.nf2024.organizeradminpanel.Fragment.CourseClassAndTestFragment
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.ActivityClassMainBinding
import com.example.nafisquaisarcoachingcenter.fragment.SubjectFragment

class ClassMainActivity() : AppCompatActivity() {
         private  lateinit var binding: ActivityClassMainBinding
         private var courseId:String?=null
         private var courseName:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityClassMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        // Retrieve the intent that started this activity
        val className = intent.getStringExtra("className")
        val BoardName = intent.getStringExtra("BoardName")
        val Test = intent.getStringExtra("test")

        courseId=intent.getStringExtra("courseId")
        courseName=intent.getStringExtra("courseName")

        binding.backarrowbtn.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()  // Pops the last fragment from the stack
            } else {
                finish()  // Closes the activity if no fragments in the back stack
            }
        }


        if (courseId != null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.wrapper,
                    CourseClassAndTestFragment(courseId = courseId!!, courseName = courseName) // Only passing `courseId` when it's not null
                )
                .commit()
        } else {
            // Proceed with className, BoardName, and Testfragment only when courseId is null
            binding.titleName.text=className!!
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.wrapper,
                    SubjectFragment(
                        className = className,
                        BoardName = BoardName,
                        Testfragment = Test
                    )
                )
                .commit()
        }



    }


    // Function to customize the back button click
    fun setBackButtonClickListener(onClick: () -> Unit) {
        binding.backarrowbtn.setOnClickListener { onClick() }
    }

    fun updateTitle(title: String) {
        binding.titleName.text = title
        Log.d("title",title)
    }


}
package com.nafis.organizerclasses.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.nafis.nafis.nf2024.organizeradminpanel.Fragment.CourseClassAndTestFragment
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.databinding.ActivityClassMainBinding
import com.nafis.organizerclasses.fragment.SubjectFragment

class ClassMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassMainBinding
    private var courseId: String? = null
    private var courseName: String? = null
    private var classname: String? = null
    private var subject: String? = null
    private var chapter: String? = null
    private var testId: String? = null
    private var boardName: String? = null
    private var test: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Retrieve the intent that started this activity
        classname = intent.getStringExtra("className")
        boardName = intent.getStringExtra("BoardName")
        test = intent.getStringExtra("test")
        courseId = intent.getStringExtra("courseId")
        courseName = intent.getStringExtra("courseName")


        // Handle incoming dynamic link for both cold start and background
//        handleDynamicLink(intent)
        // Check if all parameters are available, then navigate
//        if (testId != null && classname != null && subject != null && chapter != null) {
//            navigateToTotalTestFragment(classname!!, subject!!, chapter!!)
//        } else {
//
//        }

        initializeDefaultFragment()
        binding.backarrowbtn.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()  // Pops the last fragment from the stack
            } else {
                finish()  // Closes the activity if no fragments in the back stack
            }
        }

    }

    private fun initializeDefaultFragment() {
        if (courseId != null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.wrapper,
                    CourseClassAndTestFragment(courseId = courseId!!, courseName = courseName)
                )
                .commit()
        } else {
            // Default behavior when no courseId
            if(classname!=null){
                binding.titleName.text = classname!!
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.wrapper,
                        SubjectFragment(
                            className = classname!!,
                            BoardName = boardName,
                            Testfragment = test
                        )
                    )
                    .commit()
            }
        }
    }

    // Function to customize the back button click
    fun setBackButtonClickListener(onClick: () -> Unit) {
        binding.backarrowbtn.setOnClickListener { onClick() }
    }

    fun updateTitle(title: String) {
        binding.titleName.text = title
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        setIntent(intent)
//        handleDynamicLink(intent)
//    }

//    private fun handleDynamicLink(intent: Intent?) {
//        FirebaseDynamicLinks.getInstance()
//            .getDynamicLink(intent)
//            .addOnSuccessListener { pendingDynamicLinkData ->
//                val deepLink: Uri? = pendingDynamicLinkData?.link
//
//                deepLink?.let {
//                    testId = it.getQueryParameter("testId")
//                    classname = it.getQueryParameter("class")
//                    subject = it.getQueryParameter("subject")
//                    chapter = it.getQueryParameter("chapter")
//
//                    // Ensure all required parameters are present
//                    if (testId != null && classname != null && subject != null && chapter != null) {
//                        Log.d("DynamicLink", "Navigating to TotalTestFragment")
//                        navigateToTotalTestFragment(classname!!, subject!!, chapter!!)
//                    } else {
//                        Log.e("DynamicLink", "Missing required parameters in the dynamic link")
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e("DynamicLink", "Error retrieving dynamic link", e)
//            }
//    }
//
//    private fun navigateToTotalTestFragment(classname: String, subject: String, chapter: String) {
//        val fragment = TotalTestFragment.newInstance(classname, subject, chapter)
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.wrapper, fragment)
//            .commit()
//    }

}

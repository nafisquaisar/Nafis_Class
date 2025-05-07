package com.nafis.organizerclasses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.animation.AnimationUtils
import com.nafis.organizerclasses.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

   private val binding : ActivitySplashBinding by lazy {
       ActivitySplashBinding.inflate(layoutInflater)
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        if (intent.hasExtra("doubt")) {
            val doubtTitle = intent.getStringExtra("doubtTitle")
            val userName = intent.getStringExtra("userName")

            val doubtIntent = Intent(this, DoubtActivity::class.java).apply {
                putExtra("doubtTitle", doubtTitle)
                putExtra("userName", userName)
            }
            startActivity(doubtIntent)
            finish()
            return  // 👈 SplashActivity ka further execution yahin stop ho jayega
        }


        val welcomeText="Welcome"
        // Set welcome text with colors
        val spannableString = SpannableString(welcomeText)
        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.green_Text, theme)),
            0, 3, 0
        )
        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.black, theme)),
            3, welcomeText.length, 0
        )
        binding.welcome.text = spannableString


        val titleName="Organizer Classes"
        val spannableStringTitle = SpannableString(titleName).apply {
            setSpan(
                ForegroundColorSpan(resources.getColor(R.color.black)),
                0, 9, 0
            )
            setSpan(
                ForegroundColorSpan(resources.getColor(R.color.green_Text)),
                9, titleName.length, 0
            )
        }
        binding.coachingTitle.text = spannableStringTitle


        val animation=AnimationUtils.loadAnimation(this,R.anim.splashanim)
        binding.welcome.animation = animation
        binding.coachingTitle.animation = animation


//        method 1  for spalishing

        Handler(Looper.getMainLooper()).postDelayed({
          startActivity(Intent(this,FrontVIew::class.java))
            finish()
        },1000)


        // method 2

//        startHeavyTast()

    }

//    private fun startHeavyTast(){
//        LongOperation().execute()
//    }
//
//    private open inner class LongOperation:AsyncTask<String?,Void ?,String?>(){
//        override fun doInBackground(vararg p0: String?): String? {
//
//            for(i in 0..6)
//            {
//                try {
//                    Thread.sleep(1000)
//                }catch (e:Exception){
//                    Thread.interrupted()
//                }
//            }
//
//            return "Result"
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//            val intent=Intent(this@SplashActivity,MainActivity::class.java)
//            startActivity(intent)
//        }
//
//    }

}
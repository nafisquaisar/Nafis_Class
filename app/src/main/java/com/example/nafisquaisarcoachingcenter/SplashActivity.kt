package com.example.nafisquaisarcoachingcenter

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.text.set
import com.example.nafisquaisarcoachingcenter.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

   private val binding : ActivitySplashBinding by lazy {
       ActivitySplashBinding.inflate(layoutInflater)
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val welcometext="Welcome"
        val spannableString=SpannableString(welcometext)
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#34A671")),0,3,0)
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#021029")),3,welcometext.length,0)
        binding.welcome.text=spannableString


        val titlename="Nafis Coaching Center"
        val spannableStringtitle  =SpannableString(titlename)
        spannableStringtitle.setSpan(ForegroundColorSpan(Color.parseColor("#021029")),0,9,0)
        spannableStringtitle.setSpan(ForegroundColorSpan(Color.parseColor("#34A671")),9,titlename.length,0)
        binding.coachingTitle.text=spannableStringtitle


        val animation=AnimationUtils.loadAnimation(this,R.anim.splashanim)
        binding.welcome.animation = animation
        binding.coachingTitle.animation = animation


//        method 1  for spalishing

        Handler(Looper.getMainLooper()).postDelayed({
          startActivity(Intent(this,FrontVIew::class.java))
            finish()
        },3000)


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
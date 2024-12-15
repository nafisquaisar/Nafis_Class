package com.example.nafisquaisarcoachingcenter.coursecclass

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.example.nafisquaisarcoachingcenter.databinding.ActivityCourseOpenBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CourseOpenActivity : AppCompatActivity() {
    private  lateinit var binding :ActivityCourseOpenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCourseOpenBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val courseId = intent.getStringExtra("courseId")
        val courseName = intent.getStringExtra("courseName")
        val courseDesc = intent.getStringExtra("courseDesc")
        val courseAmount = intent.getStringExtra("courseAmount")
        val offerAmount = intent.getStringExtra("offerAmount")
        val courseImgUrl = intent.getStringExtra("courseImgUrl")
        val courseDateTimestamp = intent.getLongExtra("courseDate", 0L) // Default to 0L if not found
        val courseDate = if (courseDateTimestamp != 0L) Date(courseDateTimestamp) else null

        binding.tollbarTitle.text=courseName
       // Formatting the date to "dd-MMM-yyyy" format (e.g., 24-Dec-2024)
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val formattedDate = courseDate?.let { dateFormat.format(it) } // Format the date if it's not null

       val courseEndDate= getCourseEndDate(courseDate)

        setAllDetail(courseName,courseDesc,courseAmount,offerAmount,courseImgUrl,formattedDate,courseEndDate,courseId)
        binding.backarrowbtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getCourseEndDate(courseDate: Date?): String {
        // Use Calendar to add one year to the courseDate
        val calendar = Calendar.getInstance()
        calendar.time = courseDate ?: Date() // Use courseDate or the current date if it's null
        calendar.add(Calendar.YEAR, 1) // Add one year

// Get the new date after adding one year (including time)
        val lastDateAfterOneYear = calendar.time

// Formatting the date to "dd-MMM-yyyy" format (e.g., 24-Dec-2025)
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(lastDateAfterOneYear)
        return formattedDate
    }

    private fun setAllDetail(
        courseName: String?,
        courseDesc: String?,
        courseAmount: String?,
        offerAmount: String?,
        courseImgUrl: String?,
        courseDate: String?,
        courseEndDate: String,
        courseId: String?
    ) {
           binding.apply {
               CourseDate.text=courseDate.toString()
               CourseDescription.text=courseDesc
               CourseTitle.text=courseName
               showPriceOfCourse.text="₹ ${courseAmount}"
               showOfferPriceOfCourse.text="₹ ${offerAmount}"
               courseDuration.text="${courseDate} to ${courseEndDate}"
               showPriceOfCourse.paintFlags=showPriceOfCourse.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
               Glide.with(this@CourseOpenActivity)
                   .load(courseImgUrl)
                   .into(CourseImg)
              courseBuyBtn.setOnClickListener {
                  val intent=Intent(this@CourseOpenActivity,ClassMainActivity::class.java)
                  intent.putExtra("courseId", courseId)
                  intent.putExtra("courseName", courseName)
                  startActivity(intent)
              }
           }
    }
}
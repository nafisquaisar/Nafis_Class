package com.example.nafisquaisarcoachingcenter.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalCourseCallback
import com.example.nafisquaisarcoachingcenter.model.TotalCourse
import com.example.nafisquaisarcoachingcenter.adapter.TotalCourseAdapter
import com.example.nafisquaisarcoachingcenter.coursecclass.CourseOpenActivity
import com.example.nafisquaisarcoachingcenter.databinding.FragmentCoursesBinding
import com.example.nafisquaisarcoachingcenter.model.Subject
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class Courses : Fragment() {
  private  lateinit var binding: FragmentCoursesBinding
  private lateinit var courseList:ArrayList<TotalCourse>
  private lateinit var courseAdapter: TotalCourseAdapter
  private val db=Firebase.firestore

  private val callback by lazy{
      object :TotalCourseCallback{
          override fun onCourseClick(item: TotalCourse, position: Int) {
              val intent = Intent(requireContext(), CourseOpenActivity::class.java)
              intent.putExtra("courseId", item.courseId)
              intent.putExtra("courseName", item.courseName)
              intent.putExtra("courseDesc", item.courseDesc)
              intent.putExtra("courseAmount", item.courseAmount)
              intent.putExtra("offerAmount", item.offerAmount)
              intent.putExtra("courseImgUrl", item.courseImgUrl)
              intent.putExtra("courseDate", item.courseDate?.time)
              startActivity(intent)

          }
      }
  }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCoursesBinding.inflate(layoutInflater,container,false)

        courseList = ArrayList()
        courseAdapter = TotalCourseAdapter(requireContext(), callback, courseList)
        binding.CourseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.CourseRecyclerView.adapter = courseAdapter

        fetchData()
        return binding.root
    }

    private fun fetchData() {
        binding.progressbar.visibility = View.VISIBLE
        db.collection("courses")
            .get()
            .addOnSuccessListener { documents ->
                courseList.clear()
                for (document in documents) {
                    val courseId = document.get("courseId")?.toString() ?: ""
                    val courseName = document.getString("courseName") ?: ""
                    val courseDesc = document.getString("courseDesc") ?: ""
                    val courseAmount = document.getString("courseAmount") ?: ""
                    val courseOfferAmount = document.getString("offerAmount") ?: ""
                    val courseImgUrl = document.getString("courseImgUrl") ?: ""
                    val isCourseDisable = document.getBoolean("isCourseDisable") ?: false
                    val courseDate = document.getTimestamp("courseDate")?.toDate()
                    val subjects = document.get("subjects") as? List<Subject> ?: listOf()

                    val course = TotalCourse(
                        courseId = courseId,
                        courseName = courseName,
                        courseDesc = courseDesc,
                        courseAmount = courseAmount,
                        offerAmount = courseOfferAmount,
                        courseImgUrl = courseImgUrl,
                        isCourseDisable = isCourseDisable,
                        courseDate = courseDate,
                        subjects = subjects
                    )
                    if(!isCourseDisable){
                        courseList.add(course)
                    }
                }
                if (courseList.isEmpty()) {
                    binding.progressbar.visibility = View.GONE
                    binding.helping.visibility=View.VISIBLE
                } else {
                    courseAdapter.submitList(courseList)
                    courseAdapter.notifyDataSetChanged()
                    binding.progressbar.visibility = View.GONE
                    binding.helping.visibility=View.GONE
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch courses", Toast.LENGTH_SHORT).show()
                binding.progressbar.visibility = View.GONE
                binding.helping.visibility=View.VISIBLE
            }
    }


}
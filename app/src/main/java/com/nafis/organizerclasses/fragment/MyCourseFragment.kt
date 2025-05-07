package com.nafis.organizerclasses.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafis.organizerclasses.DIffUtilCallBack.TotalCourseCallback
import com.nafis.organizerclasses.adapter.TotalCourseAdapter
import com.nafis.organizerclasses.activity.CourseOpenActivity
import com.nafis.organizerclasses.databinding.FragmentMyCourseBinding
import com.nafis.organizerclasses.model.Subject
import com.nafis.organizerclasses.model.TotalCourse
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import java.util.Locale

class MyCourseFragment : Fragment() {
   private lateinit var binding:FragmentMyCourseBinding
    private lateinit var courseList: ArrayList<TotalCourse>
    private lateinit var courseAdapter: TotalCourseAdapter
    private val db = Firebase.firestore

    private val callback by lazy {
        object : TotalCourseCallback {
            override fun onCourseClick(item: TotalCourse, position: Int) {
                val intent = Intent(requireContext(), CourseOpenActivity::class.java)
                intent.putExtra("courseId", item.courseId)
                intent.putExtra("courseName", item.courseName)
                intent.putExtra("courseDesc", item.courseDesc)
                intent.putExtra("courseAmount", item.courseAmount)
                intent.putExtra("offerAmount", item.offerAmount)
                intent.putExtra("courseImgUrl", item.courseImgUrl)
                intent.putExtra("courseDate", item.courseDate?.time)
                intent.putExtra("isBuy", item.isBuy)
                startActivity(intent)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMyCourseBinding.inflate(inflater,container,false)

        courseList = ArrayList()
        courseAdapter = TotalCourseAdapter(requireContext(), callback, courseList)
        binding.myCourseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.myCourseRecyclerView.adapter = courseAdapter
        fetchData()

        binding.SearchCourse.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }

        })
        return binding.root
    }

    private fun fetchData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId.isNullOrEmpty()) {
            Log.e("FetchData", "User is not logged in.")
            return
        }

        binding.progressbar.visibility = View.VISIBLE

        val tempCourseList = mutableListOf<TotalCourse>()

        db.collection("courses")
            .get()
            .addOnSuccessListener { documents ->
                val tasks = mutableListOf<Task<QuerySnapshot>>()

                for (document in documents) {
                    val courseId = document.getString("courseId") ?: ""
                    val courseName = document.getString("courseName") ?: ""
                    val courseDesc = document.getString("courseDesc") ?: ""
                    val courseAmount = document.getString("courseAmount") ?: ""
                    val courseOfferAmount = document.getString("offerAmount") ?: ""
                    val courseImgUrl = document.getString("courseImgUrl") ?: ""
                    val isCourseDisable = document.getBoolean("isCourseDisable") ?: false
                    val courseDate = document.getTimestamp("courseDate")?.toDate()
                    val subjects = document.get("subjects") as? List<Subject> ?: listOf()

                    val purchaseTask = db.collection("UserPurchase")
                        .document(userId)
                        .collection("Course")
                        .document(courseId)
                        .collection("Payments")
                        .whereEqualTo("status", "Success")
                        .get()

                    tasks.add(purchaseTask)

                    purchaseTask.addOnSuccessListener { querySnapshot ->
                        val isBuy = querySnapshot.documents.any { it.getString("paymentId") != null }

                        val course = TotalCourse(
                            courseId = courseId,
                            courseName = courseName,
                            courseDesc = courseDesc,
                            courseAmount = courseAmount,
                            offerAmount = courseOfferAmount,
                            courseImgUrl = courseImgUrl,
                            isCourseDisable = isCourseDisable,
                            courseDate = courseDate,
                            subjects = subjects,
                            isBuy = isBuy
                        )
                        if (!isCourseDisable && isBuy) {
                            tempCourseList.add(course)
                        }
                    }
                }

                Tasks.whenAll(tasks).addOnCompleteListener {
                    courseList.clear()
                    courseList.addAll(tempCourseList)

                    if (courseList.isEmpty()) {
                        binding.progressbar.visibility = View.GONE
                        binding.helping.visibility = View.VISIBLE
                    } else {
                        courseAdapter.submitList(courseList)
                        courseAdapter.notifyDataSetChanged()
                        binding.progressbar.visibility = View.GONE
                        binding.helping.visibility = View.GONE
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch courses", Toast.LENGTH_SHORT).show()
                binding.progressbar.visibility = View.GONE
                binding.helping.visibility = View.VISIBLE
            }
    }

    private fun filter(query: String?) {
        if (query != null) {
            val queryLower = query.lowercase(Locale.ROOT)
            Log.d("Filter", "Query: $queryLower")

            val filteredList = courseList.filter { course ->
                val nameMatch = course.courseName.lowercase(Locale.ROOT).contains(queryLower)
                Log.d("Filter", "Checking course: ${course.courseName}, Name Match: $nameMatch")
                nameMatch
            }

            Log.d("Filter", "Filtered List: ${filteredList.map { it.courseName }}")

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
            }

            // Update the adapter with the filtered list
            courseAdapter.submitList(filteredList) // For ListAdapter
            courseAdapter.notifyDataSetChanged()  // If using a custom adapter
        }
    }


    override fun onResume() {
        super.onResume()
        fetchData()
    }
}
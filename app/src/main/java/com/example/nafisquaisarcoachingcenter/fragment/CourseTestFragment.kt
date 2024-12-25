package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nafis.nf.organizetestcenter.Adapter.TotalTestAdapter
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalTestItemCallback
import com.example.nafisquaisarcoachingcenter.Object.TestObject
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.FragmentCourseTestBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList


class CourseTestFragment(var courseId: String?, var courseName: String?) : Fragment() {
private lateinit var binding:FragmentCourseTestBinding

    private var testList : ArrayList<TestObject> = ArrayList()
    private lateinit var testAdapter: TotalTestAdapter

    private val callback by lazy {
        object : TotalTestItemCallback {
            override fun onTotalTestClick(item: TestObject, position: Int) {
                val manager=(context as AppCompatActivity).supportFragmentManager
                val transition=manager.beginTransaction()
                val Quiz= QuizFragment(item.classname,item.subname,item.chapname,item.id, item.title,courseId = courseId)

                transition.replace(R.id.wrapper,Quiz)
                transition.addToBackStack("QuizFragmentTag")
                transition.commit()
            }

            override fun onShareTest(item: TestObject) {
            }

            override fun markIsComplete(item: TestObject) {

            }


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCourseTestBinding.inflate(inflater,container,false)
        binding.courseTestRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        testAdapter=TotalTestAdapter(requireContext(),testList,callback)
        binding.courseTestRecyclerView.adapter=testAdapter
        fetchTest()
        return binding.root
    }


    private fun fetchTest() {
        binding.progressbar.visibility = View.VISIBLE
        val db = FirebaseDatabase.getInstance().getReference("CourseTest").child(courseId!!)
        testList.clear()
        Log.d("courseId",courseId.toString())
        try {
            db.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.progressbar.visibility = View.GONE
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            val test = snap.getValue(TestObject::class.java)
                            if (test != null) {
                                testList.add(test)
                            }
                        Log.d("list","List not empty")
                        }
                        if (isAdded) {
                            if (testList.isNotEmpty()) {
                                testAdapter.submitList(testList)
                                testAdapter.notifyDataSetChanged()
                                binding.helping.visibility = View.GONE
                            } else {
                                binding.helping.visibility = View.VISIBLE
                            }
                        }
                    } else {
                            binding.helping.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error fetching tests: ${error.message}")
                        binding.helping.visibility = View.VISIBLE
                        binding.progressbar.visibility = View.GONE
                }
            })
        } catch (e: Exception) {
            Log.e("fetchTest", "Error: ${e.message}")
                binding.helping.visibility = View.VISIBLE
                binding.progressbar.visibility = View.GONE

        }
    }



}
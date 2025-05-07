package com.nafis.organizerclasses.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafis.nafis.nf.organizetestcenter.Adapter.TotalTestAdapter
import com.nafis.organizerclasses.DIffUtilCallBack.TotalTestItemCallback
import com.nafis.organizerclasses.Object.TestObject
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.databinding.FragmentCourseTestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.nafis.organizerclasses.Test.QuizFragment
import com.nafis.organizerclasses.Test.ResultFragment
import com.nafis.organizerclasses.model.QuizResult
import java.util.ArrayList


class CourseTestFragment(var courseId: String?, var courseName: String?) : Fragment() {
private lateinit var binding:FragmentCourseTestBinding

    private var testList : ArrayList<TestObject> = ArrayList()
    private lateinit var testAdapter: TotalTestAdapter

    private val callback by lazy {
        object : TotalTestItemCallback {
            override fun onTotalTestClick(item: TestObject, position: Int) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
                val db = FirebaseFirestore.getInstance()

                val collectionRef=
                    db.collection("Course")
                        .document(courseId!!)
                        .collection("User")
                        .document(userId)
                        .collection("quiz_results")
                        .document(item.id) // or remove this duplicate segment if not needed
                        .collection("attempts")


                collectionRef
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (!snapshot.isEmpty) {
                            val latestResult = snapshot.documents[0].toObject(QuizResult::class.java)
                            latestResult?.let { result ->
                                val correctans = HashMap<Int, Int>()
                                result.correctAnswers.forEach { (key, value) ->
                                    correctans[key.toInt()] = value
                                }

                                val resultFragment = ResultFragment(
                                    answerStatusMap = HashMap(result.answerStatusMap),
                                    total = result.total,
                                    clasname = result.className,
                                    subname = result.subjectName,
                                    chap = result.chapter,
                                    id = result.testId,
                                    correctans = correctans,
                                    courseId = courseId,
                                    testTitle = result.testTitle,
                                    countDownTimer = result.timestamp,
                                    totalMark = item.totalMark
                                )

                                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                                    .replace(R.id.wrapper, resultFragment)
                                    .addToBackStack("ResultFragmentTag")
                                    .commit()
                            }
                        } else {
                            val quizFragment = QuizFragment(
                                clasname = item.classname,
                                subname = item.subname,
                                chap = item.chapname,
                                id = item.id,
                                testTitle = item.title,
                                totalMark = item.totalMark,
                                courseId = courseId
                            )

                            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                                .replace(R.id.wrapper, quizFragment)
                                .addToBackStack("QuizFragmentTag")
                                .commit()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
            }

            override fun onShareTest(item: TestObject) {
                // shareDynamicLink(item)
            }

            override fun markIsComplete(item: TestObject) {
                markTestComplete(item)
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

    private fun markTestComplete(item: TestObject) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            Toast.makeText(requireContext(), "Please log in to mark the test as complete", Toast.LENGTH_SHORT).show()
            return
        }

        val completedTestRef = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(userId)
            .child("Courses")
            .child(courseId!!)
            .child("completedTests")
            .child(item.id)

        // Toggle completion status
        val newStatus = !(item.isCompleted ?: false)

        completedTestRef.setValue(newStatus).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Update the item's state in the local list
                item.isCompleted = newStatus
                val position = testList.indexOfFirst { it.id == item.id }
                if (position != -1) {
                    testList[position] = item
                    testAdapter.notifyItemChanged(position)
                }

                val statusMessage = if (newStatus) "marked as complete" else "marked as incomplete"
                Toast.makeText(requireContext(), "Test $statusMessage", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to update test status", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Log.e("markTestComplete", "Error updating status: ${exception.message}")
        }
    }


    private fun fetchTest() {
        binding.progressbar.visibility = View.VISIBLE

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseDatabase.getInstance().getReference("CourseTest").child(courseId!!)
        val completedRef = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(userId!!)
            .child("Courses")
            .child(courseId!!)
            .child("completedTests")

        testList.clear()
        Log.d("courseId",courseId.toString())
        try {
            db.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.progressbar.visibility = View.GONE
                    if (snapshot.exists()) {
                        val tests = snapshot.children.mapNotNull { it.getValue(TestObject::class.java) }
                        completedRef.addListenerForSingleValueEvent(object:ValueEventListener{
                            override fun onDataChange(CompSnapshot: DataSnapshot) {
                                tests.forEach { test ->
                                    test.isCompleted = CompSnapshot.child(test.id)
                                        .getValue(Boolean::class.java) ?: false
                                    testList.add(test)
                                }

                                // Submit the updated list to the adapter
                                testAdapter.submitList(testList.toList()) // Use toList() to trigger DiffUtil updates
                                binding.helping.visibility = if (testList.isEmpty()) View.VISIBLE else View.GONE
                                binding.progressbar.visibility = View.GONE

                            }

                            override fun onCancelled(error: DatabaseError) {
                                binding.helping.visibility = View.VISIBLE
                                binding.progressbar.visibility = View.GONE
                            }

                        })

                    } else {
                            binding.helping.visibility = View.VISIBLE
                             binding.progressbar.visibility = View.GONE
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
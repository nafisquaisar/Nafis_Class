package com.example.nafisquaisarcoachingcenter.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nafis.nf.organizetestcenter.Adapter.TotalTestAdapter
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalTestItemCallback
import com.example.nafisquaisarcoachingcenter.Object.TestObject
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.coursecclass.ClassMainActivity
import com.example.nafisquaisarcoachingcenter.databinding.FragmentTotalTestBinding
import com.example.nafisquaisarcoachingcenter.progress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import java.net.URLEncoder

class TotalTestFragment(
    private var clasname: String?,
    private var subname: String?,
    private var chap: String?
) : Fragment() {

    private lateinit var binding: FragmentTotalTestBinding
    private lateinit var adapter: TotalTestAdapter
    private val list = ArrayList<TestObject>()

    private val callback by lazy {
        object : TotalTestItemCallback {
            override fun onTotalTestClick(item: TestObject, position: Int) {
                val quizFragment = QuizFragment(clasname = item.classname, subname = item.subname, chap =item.chapname, id=item.id, testTitle = item.title)
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.wrapper, quizFragment)
                    .addToBackStack("QuizFragmentTag")
                    .commit()
            }

            override fun onShareTest(item: TestObject) {
                shareTest(item)
            }

            override fun markIsComplete(item: TestObject) {
                markTestComplete(item)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTotalTestBinding.inflate(inflater, container, false)

        setupRecyclerView()
        loadDataFromFirebase()

        (activity as? ClassMainActivity)?.updateTitle("Test Series")
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = TotalTestAdapter(requireContext(), list, callback)
        binding.totaltestAdapter.layoutManager = LinearLayoutManager(context)
        binding.totaltestAdapter.adapter = adapter
    }

    private fun loadDataFromFirebase() {
        binding.progressbar.visibility = View.VISIBLE
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (clasname.isNullOrBlank() || subname.isNullOrBlank() || chap.isNullOrBlank()) {
            showEmptyState()
            binding.progressbar.visibility = View.GONE
            return
        }

        val dbReference = FirebaseDatabase.getInstance()
            .getReference("Class")
            .child(clasname!!)
            .child(subname!!)
            .child(chap!!)
        val completedRef = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(userId!!)
            .child("completedTests")

        list.clear()

        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val tests = snapshot.children.mapNotNull { it.getValue(TestObject::class.java) }

                    // Fetch completed status for all tests
                    completedRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(completedSnapshot: DataSnapshot) {
                            tests.forEach { test ->
                                test.isCompleted = completedSnapshot.child(test.id)
                                    .getValue(Boolean::class.java) ?: false
                                list.add(test)
                            }

                            // Submit the updated list to the adapter
                            adapter.submitList(list.toList()) // Use toList() to trigger DiffUtil updates
                            binding.helping.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                            binding.progressbar.visibility = View.GONE
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("Firebase", "Error fetching completed status: ${error.message}")
                            showEmptyState()
                        }
                    })
                } else {
                    showEmptyState()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching tests: ${error.message}")
                showEmptyState()
            }
        })
    }


    private fun markTestComplete(item: TestObject) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            Toast.makeText(requireContext(), "Please log in to mark the test as complete", Toast.LENGTH_SHORT).show()
            return
        }

        val completedTestRef = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(userId)
            .child("completedTests")
            .child(item.id)

        // Toggle completion status
        val newStatus = !(item.isCompleted ?: false)

        completedTestRef.setValue(newStatus).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Update the item's state in the local list
                item.isCompleted = newStatus
                val position = list.indexOfFirst { it.id == item.id }
                if (position != -1) {
                    list[position] = item
                    adapter.notifyItemChanged(position)
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

    private fun shareTest(item: TestObject) {
        // Append query parameters to the base dynamic link
        val dynamicLink = Uri.parse(
            "https://organiczerclasses.page.link/Test" +
                    "?testId=${URLEncoder.encode(item.id, "UTF-8")}" +
                    "&class=${URLEncoder.encode(item.classname, "UTF-8")}" +
                    "&subject=${URLEncoder.encode(item.subname, "UTF-8")}"
        )

        // Log the generated link for debugging
        Log.d("DynamicLink", "Generated dynamic link: $dynamicLink")

        // Share the link
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out this test: $dynamicLink")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share Link"))
    }

    private fun showEmptyState() {
        binding.progressbar.visibility = View.GONE
        binding.helping.visibility = View.VISIBLE
    }
}

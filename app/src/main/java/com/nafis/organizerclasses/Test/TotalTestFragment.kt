package com.nafis.organizerclasses.Test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafis.nafis.nf.organizetestcenter.Adapter.TotalTestAdapter
import com.nafis.organizerclasses.DIffUtilCallBack.TotalTestItemCallback
import com.nafis.organizerclasses.Object.TestObject
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.activity.ClassMainActivity
import com.nafis.organizerclasses.databinding.FragmentTotalTestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.nafis.organizerclasses.model.QuizResult

class TotalTestFragment : Fragment() {
    private var clasname: String? = null
    private var subname: String? = null
    private var chap: String? = null

    private lateinit var binding: FragmentTotalTestBinding
    private lateinit var adapter: TotalTestAdapter
    private val list = ArrayList<TestObject>()

    companion object {
        fun newInstance(classname: String, subname: String, chap: String): TotalTestFragment {
            val fragment = TotalTestFragment()
            val args = Bundle().apply {
                putString("classname", classname)
                putString("subname", subname)
                putString("chap", chap)
            }
            fragment.arguments = args
            return fragment
        }
    }


    private val callback by lazy {
        object : TotalTestItemCallback {
            override fun onTotalTestClick(item: TestObject, position: Int) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
                val db = FirebaseFirestore.getInstance()

                db.collection("users")
                    .document(userId)
                    .collection("quiz_results")
                    .document(item.id)
                    .collection("attempts")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (!snapshot.isEmpty) {
                            val latestResult = snapshot.documents[0].toObject(QuizResult::class.java)
                            latestResult?.let { result ->
                                // 🔄 Convert correctAnswers (Map<String, Int>) to HashMap<Int, Int>
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
                                    courseId = result.courseId,
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
                            // 👉 No attempts → open QuizFragment
                            val quizFragment = QuizFragment(
                                clasname = item.classname,
                                subname = item.subname,
                                chap = item.chapname,
                                id = item.id,
                                testTitle = item.title,
                                totalMark = item.totalMark
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
//                shareDynamicLink(item)
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

        arguments?.let {
            clasname = it.getString("classname")
            subname = it.getString("subname")
            chap = it.getString("chap")
        }
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

//    private fun shareTest(item: TestObject) {
//        // Append query parameters to the base dynamic link
//        val dynamicLink = Uri.parse(
//            "https://organiczerclasses.page.link/Test" +
//                    "?testId=${URLEncoder.encode(item.id, "UTF-8")}" +
//                    "&class=${URLEncoder.encode(item.classname, "UTF-8")}" +
//                    "&subject=${URLEncoder.encode(item.subname, "UTF-8")}" +
//                    "&chapter=${URLEncoder.encode(item.chapname, "UTF-8")}"
//        )
//
//
//        // Log the generated link for debugging
//        Log.d("DynamicLink", "Generated dynamic link: $dynamicLink")
//
//        // Share the link
//        val shareIntent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, "Check out this test: $dynamicLink")
//            type = "text/plain"
//        }
//        startActivity(Intent.createChooser(shareIntent, "Share Link"))
//    }


//    private fun createDynamicLink(item: TestObject): Uri {
//        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
//            .setLink(
//                Uri.parse(
//                    "https://organiczerclasses.page.link/Tests" +
//                            "?testId=${item.id}" +
//                            "&class=${Uri.encode(item.classname)}" +
//                            "&subject=${Uri.encode(item.subname)}" +
//                            "&chapter=${Uri.encode("All Chapter")}"
//                )
//            )
//            .setDomainUriPrefix("https://organiczerclasses.page.link")
//            .setAndroidParameters(
//                DynamicLink.AndroidParameters.Builder()
//                    .setMinimumVersion(21) // Adjust as needed
//                    .build()
//            )
//            .setIosParameters(
//                DynamicLink.IosParameters.Builder("com.example.ios")
//                    .setAppStoreId("123456789") // Adjust for iOS if needed
//                    .build()
//            )
//            .buildDynamicLink()
//
//        return dynamicLink.uri
//    }

//    private fun shareDynamicLink(item: TestObject) {
//        val dynamicLinkUri = createDynamicLink(item)
//
//        // Log the link for debugging
//        Log.d("DynamicLink", "Generated dynamic link: $dynamicLinkUri")
//
//        val shareIntent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, "Check out this test: $dynamicLinkUri")
//            type = "text/plain"
//        }
//        startActivity(Intent.createChooser(shareIntent, "Share Link"))
//    }


    private fun showEmptyState() {
        binding.progressbar.visibility = View.GONE
        binding.helping.visibility = View.VISIBLE
    }
}

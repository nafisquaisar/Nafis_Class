package com.nafis.organizerclasses.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.nafis.nafis.nf2024.organizeradminpanel.DiffutilCallBack.SubjectCallback
import com.nafis.organizerclasses.Object.allSubjectObject
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.adapter.CourseSubjectAdapter
import com.nafis.organizerclasses.adapter.subjectcategoryAdapter
import com.nafis.organizerclasses.activity.ClassMainActivity
import com.nafis.organizerclasses.databinding.FragmentSubjectBinding
import com.nafis.organizerclasses.model.Subject
import com.google.firebase.firestore.FirebaseFirestore


class SubjectFragment(
    var className: String = "",
    var BoardName: String? = "",
    var Testfragment: String? = "",
    var courseId: String? = null,
    var courseName: String? = null
) : Fragment() {

    private lateinit var binding: FragmentSubjectBinding
    private lateinit var subjAdapter: CourseSubjectAdapter
    private lateinit var subList: ArrayList<Subject>
    private var db = FirebaseFirestore.getInstance()

    private val callback by lazy {
        object : SubjectCallback {
            override fun onSubjectClick(item: Subject) {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                val chapterFragment = ChapterFragment(courseId = courseId, subjectId = item.subjectId, subjectName = item.subjectName)
                transaction.replace(R.id.wrapper, chapterFragment)
                transaction.addToBackStack("ChapterFragment")
                transaction.commit()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubjectBinding.inflate(inflater, container, false)

        // Check for courseId
        if (courseId != null) {
            subList = ArrayList()
            binding.subjectRecyler.layoutManager = GridLayoutManager(requireContext(), 2)
            subjAdapter = CourseSubjectAdapter(requireContext(), callback, subList)
            binding.subjectRecyler.adapter = subjAdapter
            fetchSubjects(courseId!!)
            courseName?.let { (activity as ClassMainActivity).updateTitle(it) }
        } else {
            // For all subjects when courseId is null
            binding.subjectRecyler.layoutManager = GridLayoutManager(requireContext(), 2)
            val adapter = subjectcategoryAdapter(
                allSubjectObject.getSubjectData(className, BoardName),
                requireActivity(),
                BoardName,
                Testfragment
            )
            binding.subjectRecyler.adapter = adapter
            (activity as ClassMainActivity).updateTitle(className)
        }

        return binding.root
    }

    // Fetch subjects for a given course
    private fun fetchSubjects(courseId: String) {
        binding.progressbar.visibility = View.VISIBLE
        subList.clear()
        db.collection("courses")
            .document(courseId)
            .collection("subjects")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val subject = document.toObject(Subject::class.java)
                    subList.add(subject)
                }
                if (subList.isEmpty()) {
                    binding.progressbar.visibility = View.GONE
                    binding.helping.visibility = View.VISIBLE
                } else {
                    subjAdapter.submitList(subList)
                    subjAdapter.notifyDataSetChanged()
                    binding.progressbar.visibility = View.GONE
                    binding.helping.visibility = View.GONE
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to fetch subjects: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.progressbar.visibility = View.GONE
                binding.helping.visibility = View.VISIBLE
            }
    }
}

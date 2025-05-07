package com.nafis.organizerclasses.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafis.organizerclasses.DIffUtilCallBack.CourseNoteCallback
import com.nafis.organizerclasses.DIffUtilCallBack.NoteItemCallback
import com.nafis.organizerclasses.adapter.CourseNoteAdapter
import com.nafis.organizerclasses.adapter.NoteAdapter
import com.nafis.organizerclasses.databinding.FragmentDppVIewBinding
import com.nafis.organizerclasses.model.Note
import com.nafis.organizerclasses.model.NoteModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore


class DppFragmentVIew(var classname:String?="",var subName:String?="",var chapterName:String?="",
                      var courseId: String?=null,
                      var subjectId: String?=null,
                      var chapterId: String?=null,
                      var courseChapterName: String?=null
) : Fragment() {
  private lateinit var binding: FragmentDppVIewBinding

    private lateinit var adapter: NoteAdapter
    private lateinit var list: ArrayList<NoteModel>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var courseDppList :ArrayList<Note>
    private lateinit var courseNoteAdapter: CourseNoteAdapter


    private val noteItemCallback by lazy {
        object : NoteItemCallback {
            override fun onNoteClick(item: NoteModel, position: Int) {
                openPdf(item.pdfUrl);
            }
        }
    }

    private val courseNoteCallback by lazy{
        object : CourseNoteCallback {
            override fun onNoteClick(item: Note) {
                openPdf(item.noteUrl);
            }

        }
    }
  
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDppVIewBinding.inflate(inflater,container,false)
        if(courseId!=null && subjectId!=null && chapterId!=null && courseChapterName!=null){
            courseDppList= ArrayList()
            courseNoteAdapter = CourseNoteAdapter(requireContext(),courseNoteCallback,courseDppList)
            binding.dppRecylerView.layoutManager = LinearLayoutManager(requireContext())
            binding.dppRecylerView.adapter = courseNoteAdapter
            fetchDppFromFirebase()
        }else{
            adapter = NoteAdapter(noteItemCallback)
            binding.dppRecylerView.layoutManager = LinearLayoutManager(requireContext())
            binding.dppRecylerView.adapter = adapter
            list= ArrayList()
            getNoteItem() // Fetch data
        }

        return binding.root
    }

    // ===========fetch the note ==============
    private fun fetchDppFromFirebase() {
        var db=Firebase.firestore
        courseDppList.clear()
        db.collection("courses")
            .document(courseId!!)
            .collection("subjects")
            .document(subjectId!!)
            .collection("Chapters")
            .document(chapterId!!)
            .collection("Dpps")
            .get()
            .addOnSuccessListener {Notes->
                for(note in Notes){
                    val Note=note.toObject(Note::class.java)
                    courseDppList.add(Note)
                }

                if(courseDppList.isEmpty()){
                    binding.helping.visibility=View.VISIBLE
                    binding.dppRecylerView.visibility=View.GONE
                } else {
                    courseNoteAdapter.submitList(courseDppList)
                    courseNoteAdapter.notifyDataSetChanged()
                    binding.progressbar.visibility = View.GONE
                    binding.helping.visibility=View.GONE
                    binding.dppRecylerView.visibility=View.VISIBLE
                }


            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch Notes: ${it.message}", Toast.LENGTH_SHORT).show()
                binding.progressbar.visibility = View.GONE
                binding.helping.visibility=View.VISIBLE
                binding.dppRecylerView.visibility=View.GONE
            }
    }
    private fun getNoteItem() {
        binding.progressbar.visibility=View.VISIBLE
        if (isAdded) { // Check if the fragment is attached
            firestore = FirebaseFirestore.getInstance()

            try {
                firestore.collection("Class")
                    .document(classname!!)
                    .collection("Subjects")
                    .document(subName!!)
                    .collection("Chapters")
                    .document(chapterName!!)
                    .collection("Dpps") // Assuming you have a subcollection of notes
                    .orderBy("id", Query.Direction.ASCENDING)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            if (isAdded) {
                                Toast.makeText(requireContext(), "Error loading notes: ${error.message}", Toast.LENGTH_LONG).show()
                            }
                            return@addSnapshotListener
                        }

                        if (value != null) {
                            list.clear() // Clear list to avoid duplicates

                            for (dc in value.documentChanges) {
                                when (dc.type) {
                                    DocumentChange.Type.ADDED -> {
                                        val note = dc.document.toObject(NoteModel::class.java)
                                        list.add(note)
                                    }
                                    DocumentChange.Type.MODIFIED -> {
                                        // Handle modified notes if needed
                                    }
                                    DocumentChange.Type.REMOVED -> {
                                        // Handle removed notes if needed
                                    }
                                }
                            }

                            if (list.isEmpty()) {
                                if (isAdded) {
                                    binding.helping.visibility = View.VISIBLE
                                    binding.progressbar.visibility=View.GONE
                                    binding.dppRecylerView.visibility = View.GONE
                                }
                            } else {
                                if (isAdded) {
                                    // Update the adapter with the new list
                                    adapter.submitList(ArrayList(list))
                                    binding.dppRecylerView.visibility = View.VISIBLE
                                    binding.helping.visibility = View.GONE
                                    binding.progressbar.visibility=View.GONE
                                }
                            }
                        } else {
                            if (isAdded) {
                                binding.helping.visibility = View.VISIBLE
                                binding.dppRecylerView.visibility = View.GONE
                                binding.progressbar.visibility=View.GONE
                            }
                        }
                    }
            } catch (e: Exception) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Failed to load notes: ${e.message}", Toast.LENGTH_SHORT).show()
                    binding.helping.visibility = View.VISIBLE
                    binding.progressbar.visibility=View.GONE
                }
            }
        }
    }


    //===================== Open PDF in a viewer========================
    fun openPdf(pdfUrl: String? ){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle the case where no PDF reader is installed
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            Log.d("PDF" ,e.message.toString())
        }
    }

}
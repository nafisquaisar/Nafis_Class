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
import com.nafis.organizerclasses.databinding.FragmentNoteViewBinding
import com.nafis.organizerclasses.model.Note
import com.nafis.organizerclasses.model.NoteModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.firestore

class NoteFragmentView (  var classname:String?="",var subName:String?="",var chapterName:String?="",
                          var courseId: String?=null,
                          var subjectId: String?=null,
                          var chapterId: String?=null,
                          var courseChapterName: String?=null
): Fragment() {
    private lateinit var binding: FragmentNoteViewBinding
    private lateinit var adapter: NoteAdapter
    private lateinit var list: ArrayList<NoteModel>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var courseNoteList :ArrayList<Note>
    private lateinit var courseNoteAdapter:CourseNoteAdapter


    private val noteItemCallback by lazy {
        object : NoteItemCallback {
            override fun onNoteClick(item: NoteModel, position: Int) {
                openPdf(item.pdfUrl);
            }
        }
    }

    private val courseNoteCallback by lazy{
        object :CourseNoteCallback{
            override fun onNoteClick(item: Note) {
                openPdf(item.noteUrl);
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNoteViewBinding.inflate(inflater, container, false)


        if(courseId!=null && subjectId!=null && chapterId!=null && courseChapterName!=null){
            courseNoteList= ArrayList()
            courseNoteAdapter = CourseNoteAdapter(requireContext(),courseNoteCallback,courseNoteList)
            binding.noteRecylerView.layoutManager = LinearLayoutManager(requireContext())
            binding.noteRecylerView.adapter = courseNoteAdapter
            fetchNoteFromFirebase()
        }else{
            adapter = NoteAdapter(noteItemCallback)
            binding.noteRecylerView.layoutManager = LinearLayoutManager(requireContext())
            binding.noteRecylerView.adapter = adapter
            list= ArrayList()
            getNoteItem() // Fetch data
        }

        return binding.root
    }


    // ===========fetch the note of course from firebase ==============
    private fun fetchNoteFromFirebase() {
        if (courseId.isNullOrEmpty() || subjectId.isNullOrEmpty() || chapterId.isNullOrEmpty()) {
            Log.e("Course", "Invalid IDs: courseId=$courseId, subjectId=$subjectId, chapterId=$chapterId")
            Toast.makeText(requireContext(), "Invalid data provided.", Toast.LENGTH_SHORT).show()
            binding.progressbar.visibility = View.GONE
            binding.helping.visibility = View.VISIBLE
            return
        }

        Log.d("Course", "Fetching Notes for path: courses/$courseId/subjects/$subjectId/Chapters/$chapterId/Notes")

        val db = Firebase.firestore
        courseNoteList.clear() // Clear the list before fetching
        binding.progressbar.visibility = View.VISIBLE

        db.collection("courses")
            .document(courseId!!)
            .collection("subjects")
            .document(subjectId!!)
            .collection("Chapters")
            .document(chapterId!!)
            .collection("Notes")
            .get()
            .addOnSuccessListener { notes ->
                Log.d("Course", "Fetched Notes: ${notes.documents.size}")
                for (note in notes) {
                    Log.d("Course", "Note data: ${note.data}")
                    val fetchedNote = note.toObject(Note::class.java)
                    courseNoteList.add(fetchedNote)
                }

                if (courseNoteList.isEmpty()) {
                    binding.helping.visibility = View.VISIBLE
                    Log.d("Course", "No notes found.")
                } else {
                    val updatedList = ArrayList(courseNoteList)
                    courseNoteAdapter.submitList(updatedList)
                    binding.helping.visibility = View.GONE
                }
                binding.progressbar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                Log.e("Course", "Error fetching notes: ", exception)
                Toast.makeText(requireContext(), "Failed to fetch Notes: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                binding.progressbar.visibility = View.GONE
                binding.helping.visibility = View.VISIBLE
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
                    .collection("Notes") // Assuming you have a subcollection of notes
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
                                    binding.noteRecylerView.visibility = View.GONE
                                }
                            } else {
                                if (isAdded) {
                                    // Update the adapter with the new list
                                    adapter.submitList(ArrayList(list))
                                    binding.noteRecylerView.visibility = View.VISIBLE
                                    binding.helping.visibility = View.GONE
                                    binding.progressbar.visibility=View.GONE
                                }
                            }
                        } else {
                            if (isAdded) {
                                binding.helping.visibility = View.VISIBLE
                                binding.noteRecylerView.visibility = View.GONE
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

package com.example.nafisquaisarcoachingcenter.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.NoteItemCallback
import com.example.nafisquaisarcoachingcenter.adapter.NoteAdapter
import com.example.nafisquaisarcoachingcenter.databinding.FragmentNoteViewBinding
import com.example.nafisquaisarcoachingcenter.model.NoteModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.storage.FirebaseStorage

class NoteFragmentView (  var classname:String?,var subName:String?,var chapterName:String?): Fragment() {
    private lateinit var binding: FragmentNoteViewBinding
    private lateinit var adapter: NoteAdapter
    private lateinit var list: ArrayList<NoteModel>
    private lateinit var firestore: FirebaseFirestore

    // Lazy initialization of the callback for note item clicks
    private val noteItemCallback by lazy {
        object : NoteItemCallback {
            override fun onNoteClick(item: NoteModel, position: Int) {
                Toast.makeText(requireContext(), "Note name is ${item.title}", Toast.LENGTH_SHORT).show()

                item.pdfUrl?.let { pdfUrl ->
                    val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        openPdf(uri.toString())
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to fetch PDF URL", Toast.LENGTH_SHORT).show()
                    }
                } ?: Toast.makeText(requireContext(), "No PDF URL available for this note", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNoteViewBinding.inflate(inflater, container, false)

        adapter = NoteAdapter(noteItemCallback) // Initialize adapter with proper constructor or parameters
        binding.noteRecylerView.layoutManager = LinearLayoutManager(requireContext())
        binding.noteRecylerView.adapter = adapter
        list= ArrayList()
        getNoteItem() // Fetch data

        return binding.root
    }



    private fun getNoteItem() {
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
                                    binding.noteWarning.visibility = View.VISIBLE
                                    binding.noteRecylerView.visibility = View.GONE
                                    Toast.makeText(requireContext(), "No notes found", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                if (isAdded) {
                                    // Update the adapter with the new list
                                    adapter.submitList(ArrayList(list))
                                    binding.noteRecylerView.visibility = View.VISIBLE
                                    binding.noteWarning.visibility = View.GONE
                                }
                            }
                        } else {
                            if (isAdded) {
                                binding.noteWarning.visibility = View.VISIBLE
                                binding.noteRecylerView.visibility = View.GONE
                                Toast.makeText(requireContext(), "No notes found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            } catch (e: Exception) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Failed to load notes: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




//===================== Open PDF in a viewer========================
    fun openPdf(pdfUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle the case where no PDF reader is installed
            Toast.makeText(requireContext(), "No PDF reader installed", Toast.LENGTH_SHORT).show()
        }
    }

}

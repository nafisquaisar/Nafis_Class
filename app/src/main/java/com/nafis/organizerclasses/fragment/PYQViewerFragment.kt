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
import com.nafis.organizerclasses.DIffUtilCallBack.NoteItemCallback
import com.nafis.organizerclasses.PYQActivity
import com.nafis.organizerclasses.adapter.NoteAdapter
import com.nafis.organizerclasses.activity.ClassMainActivity
import com.nafis.organizerclasses.databinding.FragmentPYQViewerBinding
import com.nafis.organizerclasses.model.NoteModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class PYQViewerFragment(var classname:String,var subName:String,var chapterName:String,var BoardName:String?) : Fragment() {
 private lateinit var binding:FragmentPYQViewerBinding
 private lateinit var adapter:NoteAdapter
 private lateinit var list: ArrayList<NoteModel>

    private lateinit var firestore: FirebaseFirestore


    private val noteItemCallback by lazy {
        object : NoteItemCallback {
            override fun onNoteClick(item: NoteModel, position: Int) {
                Toast.makeText(requireContext(), "Note name is ${item.title}", Toast.LENGTH_SHORT).show()
                openPdf(item.pdfUrl);
            }
        }
    }

    override
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPYQViewerBinding.inflate(inflater,container,false)
        binding.PyqYearRecycler.layoutManager=LinearLayoutManager(requireContext())
        adapter=NoteAdapter(callback = noteItemCallback,"PYQ Adapter")
        binding.PyqYearRecycler.adapter=adapter
        list= ArrayList()
        getPYQItem()

        (activity as ClassMainActivity).updateTitle(subName)


        return binding.root
    }

    private fun getPYQItem() {
        binding.progressbar.visibility = View.VISIBLE
        Log.d("Firestore", "Path: Class/$classname/Subjects/$subName/Board/$BoardName/PYQ")

        if (isAdded) {
            firestore = FirebaseFirestore.getInstance()

            try {
                firestore.collection("Class")
                    .document(classname)
                    .collection("Subjects")
                    .document(subName)
                    .collection("Board")
                    .document(BoardName!!)
                    .collection("PYQ")
                    .orderBy("id", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener { documents ->
                        binding.progressbar.visibility = View.GONE

                        if (!documents.isEmpty) {
                            list.clear()

                            for (document in documents) {
                                val note = document.toObject(NoteModel::class.java)
                                list.add(note)
                            }

                            if (list.isEmpty()) {
                                binding.helping.visibility = View.VISIBLE
                                binding.PyqYearRecycler.visibility = View.GONE
                                Toast.makeText(requireContext(), "No PYQs found", Toast.LENGTH_SHORT).show()
                                Log.e("Firestore", "List empty")
                            } else {
                                adapter.submitList(ArrayList(list))
                                binding.PyqYearRecycler.visibility = View.VISIBLE
                                binding.helping.visibility = View.GONE
                            }
                        } else {
                            binding.helping.visibility = View.VISIBLE
                            binding.PyqYearRecycler.visibility = View.GONE
                            Toast.makeText(requireContext(), "No PYQs found", Toast.LENGTH_SHORT).show()
                        Log.e("Firestore", "Document empty")
                        }
                    }
                    .addOnFailureListener { exception ->
                        binding.progressbar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error fetching PYQs: ${exception.message}", Toast.LENGTH_LONG).show()
                        Log.e("Firestore", "Error fetching PYQs: ${exception.message}")
                    }
            } catch (e: Exception) {
                binding.progressbar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error loading PYQs: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("Firestore", "Error loading PYQs: ${e.message}")
            }
        }
    }


    fun openPdf(pdfUrl: String? ){
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

    override fun onResume() {
        super.onResume()
        BoardName?.let {
            Log.d("PYQViewerFragment", "Updating title to: $it")
            (activity as? PYQActivity)?.updateTitle(it)
        }
    }

}
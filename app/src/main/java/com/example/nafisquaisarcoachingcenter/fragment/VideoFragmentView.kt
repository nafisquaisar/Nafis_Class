package com.example.nafisquaisarcoachingcenter.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.NoteItemCallback
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.VideoItemCallback
import com.example.nafisquaisarcoachingcenter.adapter.NoteAdapter
import com.example.nafisquaisarcoachingcenter.adapter.VideoAdapter
import com.example.nafisquaisarcoachingcenter.coursecclass.VideoViewActivity

import com.example.nafisquaisarcoachingcenter.databinding.FragmentVideoBinding
import com.example.nafisquaisarcoachingcenter.databinding.FragmentVideoViewBinding
import com.example.nafisquaisarcoachingcenter.model.VideoModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class VideoFragmentView( var classname:String?,var subName:String?,var chapterName:String?) : Fragment() {
    
    private lateinit var binding: FragmentVideoViewBinding
    private lateinit var adapter: VideoAdapter
    private lateinit var list: ArrayList<VideoModel>
    private lateinit var firestore: FirebaseFirestore

    // Lazy initialization of the callback for note item clicks
    private val videoItemCallback by lazy {
        object  :VideoItemCallback{
            override fun onNoteClick(item: VideoModel, position: Int) {
                Toast.makeText(requireContext(), "Note name is ${item.title}", Toast.LENGTH_SHORT).show()
                var intent= Intent(requireContext(),VideoViewActivity::class.java)
                intent.putExtra("VideoUrl","https://youtu.be/fub5nYqYRU8?si=wEo01zqp8UwLuB9E")
                intent.putExtra("ChapterName",item.chapname)
                intent.putExtra("TitleName",item.title)
               startActivity(intent)
            }
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentVideoViewBinding.inflate(inflater,container,false)
        adapter = VideoAdapter(videoItemCallback) // Initialize adapter with proper constructor or parameters
        binding.videoRecylerView.layoutManager = LinearLayoutManager(requireContext())
        binding.videoRecylerView.adapter = adapter
        list= ArrayList()
        getNoteItem() // Fetch data
        return binding.root

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
                    .collection("Video") // Assuming you have a subcollection of notes
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
                                        val note = dc.document.toObject(VideoModel::class.java)
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
                                    binding.progressbar.visibility=View.GONE
                                    binding.videoRecylerView.visibility = View.GONE
                                    Toast.makeText(requireContext(), "No Video found", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                if (isAdded) {
                                    // Update the adapter with the new list
                                    adapter.submitList(ArrayList(list))
                                    binding.videoRecylerView.visibility = View.VISIBLE
                                    binding.noteWarning.visibility = View.GONE
                                    binding.progressbar.visibility=View.GONE
                                }
                            }
                        } else {
                            if (isAdded) {
                                binding.noteWarning.visibility = View.VISIBLE
                                binding.videoRecylerView.visibility = View.GONE
                                binding.progressbar.visibility=View.GONE
                                Toast.makeText(requireContext(), "No Video found", Toast.LENGTH_SHORT).show()
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
}
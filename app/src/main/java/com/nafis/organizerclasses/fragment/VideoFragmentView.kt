package com.nafis.organizerclasses.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafis.organizerclasses.DIffUtilCallBack.CourseVideoCallback
import com.nafis.organizerclasses.DIffUtilCallBack.VideoItemCallback
import com.nafis.organizerclasses.adapter.CourseVideoAdapter
import com.nafis.organizerclasses.adapter.VideoAdapter
import com.nafis.organizerclasses.activity.VideoViewActivity

import com.nafis.organizerclasses.databinding.FragmentVideoViewBinding
import com.nafis.organizerclasses.model.Video
import com.nafis.organizerclasses.model.VideoModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore


class VideoFragmentView(
    var classname:String?="",var subName:String?="",var chapterName:String?="",
    var courseId: String?=null,
    var subjectId: String?=null,
    var chapterId: String?=null,
    var courseChapterName: String?=null
    ) : Fragment() {
    
    private lateinit var binding: FragmentVideoViewBinding
    private lateinit var adapter: VideoAdapter
    private lateinit var list: ArrayList<VideoModel>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var courseVideoList :ArrayList<Video>
    private lateinit var courseVideoAdapter: CourseVideoAdapter


    // Lazy initialization of the callback for note item clicks
    private val videoItemCallback by lazy {
        object  :VideoItemCallback{
            override fun onNoteClick(item: VideoModel, position: Int) {
                var intent= Intent(requireContext(),VideoViewActivity::class.java)
                intent.putExtra("VideoUrl",item.videoUrl)
                intent.putExtra("ChapterName",item.chapname)
                intent.putExtra("TitleName",item.title)
                intent.putExtra("Des",item.des)
               startActivity(intent)
            }
        }
    }

    private val courseVideoCallback by lazy {
        object : CourseVideoCallback{
            override fun onVideoClick(item: Video) {
                var intent= Intent(requireContext(),VideoViewActivity::class.java)
                intent.putExtra("VideoUrl",item.videoUrl)
                intent.putExtra("ChapterName",courseChapterName)
                intent.putExtra("TitleName",item.videoTitle)
                intent.putExtra("Des",item.des)
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




        if(courseId!=null && subjectId!=null && chapterId!=null && courseChapterName!=null) {
            courseVideoList= ArrayList()
            courseVideoAdapter=CourseVideoAdapter(requireContext(),courseVideoCallback,courseVideoList)
            binding.videoRecylerView.layoutManager = LinearLayoutManager(requireContext())
            binding.videoRecylerView.adapter = courseVideoAdapter
            fetchVideo()

        }else{
            adapter = VideoAdapter(videoItemCallback) // Initialize adapter with proper constructor or parameters
            binding.videoRecylerView.layoutManager = LinearLayoutManager(requireContext())
            binding.videoRecylerView.adapter = adapter
            list= ArrayList()
            getVideoItem() // Fetch data
        }


        return binding.root

    }

    private fun fetchVideo() {
        binding.progressbar.visibility=View.VISIBLE
        val db= Firebase.firestore
        courseVideoList.clear()
        db.collection("courses")
            .document(courseId!!)
            .collection("subjects")
            .document(subjectId!!)
            .collection("Chapters")
            .document(chapterId!!)
            .collection("Videos")
            .get()
            .addOnSuccessListener {Videos->
                for (vid in Videos){
                    val video=vid.toObject(Video::class.java)
                    courseVideoList.add(video)
                }

                if(courseVideoList.isEmpty()){
                    binding.progressbar.visibility = View.GONE
                    binding.helping.visibility=View.VISIBLE
                } else {
                    courseVideoAdapter.submitList(courseVideoList)
                    courseVideoAdapter.notifyDataSetChanged()
                    binding.progressbar.visibility = View.GONE
                    binding.helping.visibility=View.GONE
                }

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch Video: ${it.message}", Toast.LENGTH_SHORT).show()
                binding.progressbar.visibility = View.GONE
                binding.helping.visibility=View.VISIBLE
            }

    }


    private fun getVideoItem() {
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
                                        try {
                                            val id = when (val rawId = dc.document.get("id")) {
                                                is Number -> rawId.toLong() // Handle Long/Int
                                                is String -> rawId.toLongOrNull() ?: 0L // Convert String to Long safely
                                                else -> 0L // Default for unexpected type
                                            }

                                            val videoModel = VideoModel(
                                                id = id,
                                                title = dc.document.getString("title"),
                                                des = dc.document.getString("des"),
                                                date = dc.document.getString("date"),
                                                time = dc.document.getString("time"),
                                                chapname = dc.document.getString("chapname"),
                                                clasname = dc.document.getString("clasname"),
                                                subname = dc.document.getString("subname"),
                                                videoUrl = dc.document.getString("videoUrl")
                                            )

                                            list.add(videoModel)
                                        } catch (e: Exception) {
                                            Log.e("DeserializationError", "Failed to map document: ${e.message}")
                                        }
                                    }
                                    DocumentChange.Type.MODIFIED -> {
                                        // Handle modified items (if applicable)
                                    }
                                    DocumentChange.Type.REMOVED -> {
                                        // Handle removed items (if applicable)
                                    }
                                }
                            }


                            if (list.isEmpty()) {
                                if (isAdded) {
                                    binding.helping.visibility = View.VISIBLE
                                    binding.progressbar.visibility=View.GONE
                                    binding.videoRecylerView.visibility = View.GONE
                                }
                            } else {
                                if (isAdded) {
                                    // Update the adapter with the new list
                                    adapter.submitList(ArrayList(list))
                                    binding.videoRecylerView.visibility = View.VISIBLE
                                    binding.helping.visibility = View.GONE
                                    binding.progressbar.visibility=View.GONE
                                }
                            }
                        } else {
                            if (isAdded) {
                                binding.helping.visibility = View.VISIBLE
                                binding.videoRecylerView.visibility = View.GONE
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
}
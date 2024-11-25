package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nafis.nf.organizetestcenter.Adapter.TotalTestAdapter
import com.example.nafisquaisarcoachingcenter.DIffUtilCallBack.TotalTestItemCallback
import com.example.nafisquaisarcoachingcenter.Object.TestObject
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.coursecclass.ClassMainActivity
import com.example.nafisquaisarcoachingcenter.databinding.FragmentTotalTestBinding
import com.example.nafisquaisarcoachingcenter.progress
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TotalTestFragment(
    private var clasname:String?,private var subname:String?,private var chap:String?
) : Fragment() {
    private lateinit var binding: FragmentTotalTestBinding
    private lateinit var adapter: TotalTestAdapter
    private lateinit var list: ArrayList<TestObject>

    private val callback by lazy {
        object :TotalTestItemCallback{
            override fun onTotalTestClick(item: TestObject, position: Int) {
                val manager=(context as AppCompatActivity).supportFragmentManager
                val transition=manager.beginTransaction()
                val Quiz= QuizFragment(item.classname,item.subname,item.chapname,item.id)

                transition.replace(R.id.wrapper,Quiz)
                transition.addToBackStack("QuizFragmentTag")
                transition.commit()
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTotalTestBinding.inflate(inflater, container, false)

//        list = getData()
        // Initialize the adapter
        list= ArrayList()
        adapter = TotalTestAdapter(requireContext(),list,callback)
        binding.totaltestAdapter.layoutManager = LinearLayoutManager(context)
        loadDataFromFirebase()
        // Fetch data and set it to the adapter

        // Update toolbar title
        (activity as? ClassMainActivity)?.updateTitle("Test Series")




        return binding.root
    }



    private fun loadDataFromFirebase() {
        binding.progressbar.visibility = View.VISIBLE
        binding.helping.visibility = View.GONE
        list = ArrayList()

        // Ensure 'clasname', 'subname', and 'chap' are not null
        if (clasname == null || subname == null || chap == null) {
            binding.progressbar.visibility = View.GONE
            binding.helping.visibility = View.VISIBLE
            return
        }


        val dbReference = clasname?.let { FirebaseDatabase.getInstance().getReference("Class").child(it) }
            ?.child(subname ?: "")
            ?.child(chap ?: "")

        try {
            dbReference?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            val testPaper = snap.getValue(TestObject::class.java)
                            if (testPaper != null) {
                                list.add(testPaper)
                            }
                        }

                        if (isAdded) { // Ensure fragment is still attached to the activity
                            if (list.isNotEmpty()) {
                                adapter = context?.let { TotalTestAdapter(it, list,callback) }!!
                                binding.totaltestAdapter.adapter = adapter
                                adapter.submitList(list)
                            } else {
                                showEmptyState()
                            }
                        }
                    } else {
                        showEmptyState()
                    }

                    binding.progressbar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle any errors here, like showing a message to the user
                    binding.progressbar.visibility = View.GONE
                    binding.helping.visibility = View.VISIBLE
                }
            })
        }catch (e:Exception){
            progress.ToastShow(requireContext(),e.message.toString())
            binding.progressbar.visibility = View.GONE
            binding.helping.visibility = View.VISIBLE
        }
    }

    private fun showEmptyState() {
        binding.totaltestAdapter.visibility = View.GONE
        binding.helping.visibility = View.VISIBLE
    }

}

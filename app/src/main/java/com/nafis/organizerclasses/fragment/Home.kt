package com.nafis.organizerclasses.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.nafis.organizerclasses.Note_home_activity
import com.nafis.organizerclasses.PYQActivity
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.Test_home_activity
import com.nafis.organizerclasses.Video_Home_Activity
import com.nafis.organizerclasses.adapter.categoryAdapter
import com.nafis.organizerclasses.activity.ClassMainActivity
import com.nafis.organizerclasses.databinding.FragmentHomeBinding


class Home : Fragment() {
      private lateinit var binding:FragmentHomeBinding
      private lateinit var adapter: categoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding= FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ************************  Recycler view Set start *********************************
//       binding.CourseRecyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,true)
//        adapter=categoryAdapter(homeClassObject.getData(),requireActivity())
//        binding.CourseRecyclerView.adapter=adapter
        // ************************  Recycler view Set start *********************************



        // ******************  Searching Function Start *********************************
//        binding.courseSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                filter(newText)
//                return true
//            }
//        })
        // ******************  Searching Function end *********************************


        // *********************** Image slide functionality Start **********************************
           var slideModel=ArrayList<SlideModel>()

          slideModel.add(SlideModel(R.drawable.organizer_poster,ScaleTypes.FIT))
          slideModel.add(SlideModel(R.drawable.chem_home_slider,ScaleTypes.FIT))
          slideModel.add(SlideModel(R.drawable.math_home_slider,ScaleTypes.FIT))
          slideModel.add(SlideModel(R.drawable.bio_home_slider,ScaleTypes.FIT))
          slideModel.add(SlideModel(R.drawable.physic_home_slider,ScaleTypes.FIT))
          slideModel.add(SlideModel(R.drawable.science_home_slide,ScaleTypes.FIT))

           binding.homeImageSlider.setImageList(slideModel,ScaleTypes.FIT)

        // *********************** Image slide functionality End **********************************


        // ********************** Class card button access start *******************************
        binding.moreClassBtn.setOnClickListener{
            var intent=Intent(context,Video_Home_Activity::class.java)
            startActivity(intent)
        }

        binding.class10thcard.setOnClickListener{
            var intent=Intent(context,ClassMainActivity::class.java)
            intent.putExtra("className","Class 10")
            startActivity(intent)
        }

        binding.class9thcard.setOnClickListener{
            var intent=Intent(context,ClassMainActivity::class.java)
            intent.putExtra("className","Class 9")
            startActivity(intent)
        }
        // ********************** Class card button access End *******************************


        // ********************** AllotherCard view button access start *******************************
        binding.testLayoutButton.setOnClickListener{
            var intent=Intent(context,Test_home_activity::class.java)
            startActivity(intent)

        }

        binding.videoHomeButton.setOnClickListener{
            var intent=Intent(context,Video_Home_Activity::class.java)
            startActivity(intent)
        }

        binding.noteLayoutButton.setOnClickListener{
            var intent=Intent(context,Note_home_activity::class.java)
            startActivity(intent)
        }

        binding.PYQHomeButton.setOnClickListener{
            var intent=Intent(context,PYQActivity::class.java)
            startActivity(intent)
        }

        // ********************** AllotherCard view button access End *******************************


    }

//    private fun filter(query: String?) {
//        query?.let {
//            val filteredList = homeClassObject.getData().filter {
//                it.catText.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
//            }
//            if (filteredList.isEmpty()) {
//                Toast.makeText(context, "No data Found", Toast.LENGTH_SHORT).show()
//            } else {
//                adapter.filterfun(ArrayList(filteredList))
//            }
//        }
//
//    }


//    fun loadfragment(fragment: Fragment, flag:Int){
//        val fragmentManager = fragmentManager
//        val fragmentTransaction = fragmentManager?.beginTransaction()
//
//        if (flag == 0) {
//            fragmentTransaction?.add(R.id.container, fragment)
//        } else {
//            fragmentTransaction?.replace(R.id.container, fragment)
//        }
//        fragmentTransaction?.commit()
//    }

}
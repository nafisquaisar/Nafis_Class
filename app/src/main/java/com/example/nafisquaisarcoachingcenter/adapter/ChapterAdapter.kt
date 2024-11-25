package com.example.nafisquaisarcoachingcenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.databinding.HomeItemBinding
import com.example.nafisquaisarcoachingcenter.fragment.LectureFragment
import com.example.nafisquaisarcoachingcenter.fragment.TotalTestFragment
import com.example.nafisquaisarcoachingcenter.model.ChapterModel

class ChapterAdapter(private val context: Context, val list:ArrayList<ChapterModel>,private var testfrag:String?=""):
    RecyclerView.Adapter<ChapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view: HomeItemBinding =
            HomeItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ChapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val model=list[position]
        holder.bind(model)

        holder.itemView.setOnClickListener{
           var fm=(context as AppCompatActivity).supportFragmentManager
            var transaction=fm.beginTransaction()
            var lectureFragment=LectureFragment(model.clasname,model.subname,model.chapname)

            if(testfrag?.isNotEmpty() == true){
                transaction.replace(R.id.wrapper,TotalTestFragment(model.clasname,model.subname,model.chapname))
                transaction.addToBackStack("totalTestFragment")
                transaction.commit()
            }else{
                transaction.replace(R.id.wrapper,lectureFragment)
                transaction.addToBackStack("lectureFragment")
                transaction.commit()
            }


        }

    }
}
package com.nafis.organizerclasses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.databinding.HomeItemBinding
import com.nafis.organizerclasses.fragment.LectureFragment
import com.nafis.organizerclasses.Test.TotalTestFragment
import com.nafis.organizerclasses.model.ChapterModel

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
                val fragment = TotalTestFragment.newInstance(model.clasname!!, model.subname!!, model.chapname!!)
                transaction.replace(R.id.wrapper,fragment)
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
package com.example.nafis.nf.organizetestcenter.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nafis.nf.organizetestcenter.Model.TotalNoTestModel
import com.example.nafis.nf.organizetestcenter.QuizFragment
import com.example.nafis.nf.organizetestcenter.R
import com.example.nafis.nf.organizetestcenter.databinding.NoOfTestModelBinding

class TotalTestAdapter(val context: Context,val list:ArrayList<TotalNoTestModel>):
    RecyclerView.Adapter<TotalTestViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TotalTestViewHolder {
        val view=NoOfTestModelBinding.inflate(LayoutInflater.from(context),parent,false)
        return TotalTestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TotalTestViewHolder, position: Int) {
        val model=list[position]
        holder.bind(model)

        holder.itemView.setOnClickListener{

            val manager=(context as AppCompatActivity).supportFragmentManager
            val transition=manager.beginTransaction()
            val Quiz= QuizFragment(model.classname,model.subname,model.chapname,model.id)

            transition.replace(R.id.wrapper,Quiz)
            transition.addToBackStack("NoOfTestFragmentTag")
            transition.commit()
        }
    }
}
package com.nafis.organizerclasses.DIffUtilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.nafis.organizerclasses.model.DoubtModel

class DiffDoubtCallback :DiffUtil.ItemCallback<DoubtModel>() {
    override fun areItemsTheSame(oldItem: DoubtModel, newItem: DoubtModel): Boolean {
               return oldItem.doubtId==newItem.doubtId
    }

    override fun areContentsTheSame(oldItem: DoubtModel, newItem: DoubtModel): Boolean {
              return oldItem.studQuesImgUrl==newItem.studQuesImgUrl &&
                      oldItem.studQuesTitle==newItem.studQuesTitle &&
                      oldItem.studQuesDesc==newItem.studQuesDesc &&
                      oldItem.teachAnsImgUrl==newItem.teachAnsImgUrl &&
                      oldItem.teachAnsDesc==newItem.teachAnsDesc
    }
}
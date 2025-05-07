package com.nafis.organizerclasses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.model.QuizResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizResultAdapter(
    private val onViewResultClicked: (QuizResult,Int) -> Unit
) : ListAdapter<QuizResult, QuizResultAdapter.QuizResultViewHolder>(DiffCallback()) {

    class QuizResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testCount: TextView = itemView.findViewById(R.id.countAttemp)         // e.g., "1."
        val testAttempt: TextView = itemView.findViewById(R.id.Attempt)          // e.g., "Attempt 1"
        val testPercentage: TextView = itemView.findViewById(R.id.AttemptPer)    // e.g., "(50%)"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz_result, parent, false)
        return QuizResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizResultViewHolder, position: Int) {
        val result = getItem(position)

        // Set Attempt number
        holder.testCount.text = "${position + 1}."

        // Set Attempt number text, e.g., "Attempt 1"
        holder.testAttempt.text = "Attempt ${position + 1}"
        var list=calculateScore(result.answerStatusMap)
        // Calculate percentage and set it
        val percentage = if (result.total > 0) {
            ((list[0] / result.total.toDouble()) * 100).toInt()
        } else {
            0
        }
        holder.testPercentage.text = "($percentage%)"
        holder.itemView.setOnClickListener {
            onViewResultClicked(result, position + 1)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<QuizResult>() {
        override fun areItemsTheSame(oldItem: QuizResult, newItem: QuizResult): Boolean {
            return oldItem.testId == newItem.testId && oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: QuizResult, newItem: QuizResult): Boolean {
            return oldItem == newItem
        }
    }


    private fun calculateScore(statusMap: Map<String, String>): List<Int> {
        var right = statusMap.values.count { it == "correct" }
        var incorrectCnt = statusMap.values.count { it == "incorrect" }
        var skipped = statusMap.values.count { it == "skipped" }



        return listOf(right, incorrectCnt, skipped)
    }
}

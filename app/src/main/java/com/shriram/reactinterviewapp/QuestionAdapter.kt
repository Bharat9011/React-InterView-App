package com.shriram.reactinterviewapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuestionAdapter(
    private var allQuestions: List<QuestionItem>
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    private var filteredList: List<QuestionItem> = allQuestions.toList()

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestionBadge: TextView = itemView.findViewById(R.id.tvQuestionBadge)
        val tvQuestionText: TextView = itemView.findViewById(R.id.tvQuestionText)
        val tvAnswerText: TextView = itemView.findViewById(R.id.tvAnswerText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = filteredList[position]
        holder.tvQuestionBadge.text = "Q${position + 1}"
        holder.tvQuestionText.text = question.question
        holder.tvAnswerText.text = question.answer
    }

    override fun getItemCount(): Int = filteredList.size

    fun filter(query: String, onResult: ((Int) -> Unit)? = null) {
        val trimmed = query.trim()
        filteredList = if (trimmed.isEmpty()) {
            allQuestions.toList()
        } else {
            allQuestions.filter {
                it.question.contains(trimmed, ignoreCase = true) ||
                        it.answer.contains(trimmed, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
        onResult?.invoke(filteredList.size)
    }
}

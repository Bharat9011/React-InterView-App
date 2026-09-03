package com.shriram.reactinterviewapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SubtopicAdapter(
    private var allSubtopics: List<Subtopic>,
    private val onItemClick: (Subtopic) -> Unit
) : RecyclerView.Adapter<SubtopicAdapter.SubtopicViewHolder>() {

    private var filteredList: List<Subtopic> = allSubtopics.toList()

    class SubtopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSubtopicNumber: TextView = itemView.findViewById(R.id.tvSubtopicNumber)
        val tvSubtopicTitle: TextView = itemView.findViewById(R.id.tvSubtopicTitle)
        val tvSubtopicDescription: TextView = itemView.findViewById(R.id.tvSubtopicDescription)
        val tvSubtopicQuestionCount: TextView = itemView.findViewById(R.id.tvSubtopicQuestionCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtopicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subtopic, parent, false)
        return SubtopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubtopicViewHolder, position: Int) {
        val subtopic = filteredList[position]
        holder.tvSubtopicNumber.text = String.format("%02d", position + 1)
        holder.tvSubtopicTitle.text = subtopic.title
        holder.tvSubtopicDescription.text = subtopic.description
        val count = subtopic.questions.size
        holder.tvSubtopicQuestionCount.text = if (count == 1) "1 Q&A" else "$count Q&As"

        holder.itemView.setOnClickListener {
            onItemClick(subtopic)
        }
    }

    override fun getItemCount(): Int = filteredList.size

    fun filter(query: String, onResult: ((Int) -> Unit)? = null) {
        val trimmed = query.trim()
        filteredList = if (trimmed.isEmpty()) {
            allSubtopics.toList()
        } else {
            allSubtopics.filter {
                it.title.contains(trimmed, ignoreCase = true) ||
                        it.description.contains(trimmed, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
        onResult?.invoke(filteredList.size)
    }
}

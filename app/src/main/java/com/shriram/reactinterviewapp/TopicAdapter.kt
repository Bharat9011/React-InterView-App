package com.shriram.reactinterviewapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class TopicAdapter(
    private val fullTopicList: List<Topic>,
    private val onItemClick: (Topic) -> Unit
) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private val displayedList = fullTopicList.toMutableList()

    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTopicNumber: TextView = itemView.findViewById(R.id.tvTopicNumber)
        private val tvTopicTitle: TextView = itemView.findViewById(R.id.tvTopicTitle)
        private val tvTopicSubtitle: TextView = itemView.findViewById(R.id.tvTopicSubtitle)
        private val tvTopicTag: TextView = itemView.findViewById(R.id.tvTopicTag)

        fun bind(topic: Topic) {
            tvTopicNumber.text = String.format(Locale.getDefault(), "%02d", topic.id)
            tvTopicTitle.text = topic.title
            tvTopicSubtitle.text = topic.subtitle
            tvTopicTag.text = topic.tag

            itemView.setOnClickListener {
                onItemClick(topic)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topic, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bind(displayedList[position])
    }

    override fun getItemCount(): Int = displayedList.size

    fun filter(query: String, onFilterComplete: ((Int) -> Unit)? = null) {
        val cleanQuery = query.trim().lowercase(Locale.getDefault())
        displayedList.clear()

        if (cleanQuery.isEmpty()) {
            displayedList.addAll(fullTopicList)
        } else {
            val filtered = fullTopicList.filter { topic ->
                topic.title.lowercase(Locale.getDefault()).contains(cleanQuery) ||
                        topic.subtitle.lowercase(Locale.getDefault()).contains(cleanQuery) ||
                        topic.tag.lowercase(Locale.getDefault()).contains(cleanQuery)
            }
            displayedList.addAll(filtered)
        }

        notifyDataSetChanged()
        onFilterComplete?.invoke(displayedList.size)
    }
}

package com.shriram.reactinterviewapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var topicAdapter: TopicAdapter
    private lateinit var etSearch: EditText
    private lateinit var ivClearSearch: ImageView
    private lateinit var tvTopicCountBadge: TextView
    private lateinit var recyclerViewTopics: RecyclerView
    private lateinit var llEmptyState: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupRecyclerView()
        setupSearch()
    }

    private fun initViews() {
        etSearch = findViewById(R.id.etSearch)
        ivClearSearch = findViewById(R.id.ivClearSearch)
        tvTopicCountBadge = findViewById(R.id.tvTopicCountBadge)
        recyclerViewTopics = findViewById(R.id.recyclerViewTopics)
        llEmptyState = findViewById(R.id.llEmptyState)
    }

    private fun setupRecyclerView() {
        recyclerViewTopics.layoutManager = LinearLayoutManager(this)

        val topics = TopicRepository.getAllTopics(this)

        topicAdapter = TopicAdapter(topics) { topic ->
            val intent = Intent(this, SubtopicsActivity::class.java).apply {
                putExtra(SubtopicsActivity.EXTRA_TOPIC_ID, topic.id)
            }
            startActivity(intent)
        }

        recyclerViewTopics.adapter = topicAdapter
        updateTopicCount(topics.size)
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString().orEmpty()
                ivClearSearch.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE

                topicAdapter.filter(query) { resultCount ->
                    updateTopicCount(resultCount)
                    if (resultCount == 0) {
                        recyclerViewTopics.visibility = View.GONE
                        llEmptyState.visibility = View.VISIBLE
                    } else {
                        recyclerViewTopics.visibility = View.VISIBLE
                        llEmptyState.visibility = View.GONE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        ivClearSearch.setOnClickListener {
            etSearch.text?.clear()
        }
    }

    private fun updateTopicCount(count: Int) {
        tvTopicCountBadge.text = if (count == 1) "1 Topic" else "$count Topics"
    }
}
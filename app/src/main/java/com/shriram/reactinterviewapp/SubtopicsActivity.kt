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

class SubtopicsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TOPIC_ID = "extra_topic_id"
    }

    private lateinit var tvTopicTitle: TextView
    private lateinit var tvTopicSubtitle: TextView
    private lateinit var tvSubtopicCountBadge: TextView
    private lateinit var etSearch: EditText
    private lateinit var ivClearSearch: ImageView
    private lateinit var recyclerViewSubtopics: RecyclerView
    private lateinit var llEmptyState: LinearLayout

    private lateinit var subtopicAdapter: SubtopicAdapter
    private var currentTopicId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_subtopics)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        currentTopicId = intent.getIntExtra(EXTRA_TOPIC_ID, 1)

        initViews()
        loadTopicData()
    }

    private fun initViews() {
        tvTopicTitle = findViewById(R.id.tvTopicTitle)
        tvTopicSubtitle = findViewById(R.id.tvTopicSubtitle)
        tvSubtopicCountBadge = findViewById(R.id.tvSubtopicCountBadge)
        etSearch = findViewById(R.id.etSearch)
        ivClearSearch = findViewById(R.id.ivClearSearch)
        recyclerViewSubtopics = findViewById(R.id.recyclerViewSubtopics)
        llEmptyState = findViewById(R.id.llEmptyState)

        findViewById<View>(R.id.btnBack)?.setOnClickListener {
            finish()
        }
    }

    private fun loadTopicData() {
        val topic = TopicRepository.getTopicById(this, currentTopicId) ?: run {
            finish()
            return
        }

        tvTopicTitle.text = topic.title
        tvTopicSubtitle.text = topic.subtitle
        val subtopicCount = topic.subtopics.size
        tvSubtopicCountBadge.text = if (subtopicCount == 1) "1 Subtopic" else "$subtopicCount Subtopics"

        recyclerViewSubtopics.layoutManager = LinearLayoutManager(this)
        subtopicAdapter = SubtopicAdapter(topic.subtopics) { subtopic ->
            val intent = Intent(this, QuestionsActivity::class.java).apply {
                putExtra(QuestionsActivity.EXTRA_TOPIC_ID, topic.id)
                putExtra(QuestionsActivity.EXTRA_SUBTOPIC_ID, subtopic.id)
            }
            startActivity(intent)
        }
        recyclerViewSubtopics.adapter = subtopicAdapter

        setupSearch()
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString().orEmpty()
                ivClearSearch.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE

                subtopicAdapter.filter(query) { resultCount ->
                    tvSubtopicCountBadge.text = if (resultCount == 1) "1 Subtopic" else "$resultCount Subtopics"
                    if (resultCount == 0) {
                        recyclerViewSubtopics.visibility = View.GONE
                        llEmptyState.visibility = View.VISIBLE
                    } else {
                        recyclerViewSubtopics.visibility = View.VISIBLE
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
}

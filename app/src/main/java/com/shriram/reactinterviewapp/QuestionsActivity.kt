package com.shriram.reactinterviewapp

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

class QuestionsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TOPIC_ID = "extra_topic_id"
        const val EXTRA_SUBTOPIC_ID = "extra_subtopic_id"
    }

    private lateinit var tvSubtopicTitle: TextView
    private lateinit var tvParentTopicBreadcrumb: TextView
    private lateinit var tvQuestionCountBadge: TextView
    private lateinit var etSearch: EditText
    private lateinit var ivClearSearch: ImageView
    private lateinit var recyclerViewQuestions: RecyclerView
    private lateinit var llEmptyState: LinearLayout

    private lateinit var questionAdapter: QuestionAdapter
    private var topicId: Int = 1
    private var subtopicId: Int = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_questions)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        topicId = intent.getIntExtra(EXTRA_TOPIC_ID, 1)
        subtopicId = intent.getIntExtra(EXTRA_SUBTOPIC_ID, 101)

        initViews()
        loadQuestions()
    }

    private fun initViews() {
        tvSubtopicTitle = findViewById(R.id.tvSubtopicTitle)
        tvParentTopicBreadcrumb = findViewById(R.id.tvParentTopicBreadcrumb)
        tvQuestionCountBadge = findViewById(R.id.tvQuestionCountBadge)
        etSearch = findViewById(R.id.etSearch)
        ivClearSearch = findViewById(R.id.ivClearSearch)
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions)
        llEmptyState = findViewById(R.id.llEmptyState)

        findViewById<View>(R.id.btnBack)?.setOnClickListener {
            finish()
        }
    }

    private fun loadQuestions() {
        val topic = TopicRepository.getTopicById(this, topicId)
        val subtopic = TopicRepository.getSubtopic(this, topicId, subtopicId) ?: run {
            finish()
            return
        }

        tvSubtopicTitle.text = subtopic.title
        tvParentTopicBreadcrumb.text = topic?.title ?: "React Interview Prep"
        val count = subtopic.questions.size
        tvQuestionCountBadge.text = if (count == 1) "1 Q&A" else "$count Q&As"

        recyclerViewQuestions.layoutManager = LinearLayoutManager(this)
        questionAdapter = QuestionAdapter(subtopic.questions)
        recyclerViewQuestions.adapter = questionAdapter

        setupSearch()
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString().orEmpty()
                ivClearSearch.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE

                questionAdapter.filter(query) { resultCount ->
                    tvQuestionCountBadge.text = if (resultCount == 1) "1 Q&A" else "$resultCount Q&As"
                    if (resultCount == 0) {
                        recyclerViewQuestions.visibility = View.GONE
                        llEmptyState.visibility = View.VISIBLE
                    } else {
                        recyclerViewQuestions.visibility = View.VISIBLE
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

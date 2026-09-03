package com.shriram.reactinterviewapp

data class QuestionItem(
    val id: Int,
    val question: String,
    val answer: String
)

data class Subtopic(
    val id: Int,
    val topicId: Int,
    val title: String,
    val description: String,
    val questions: List<QuestionItem>
)

data class Topic(
    val id: Int,
    val title: String,
    val subtitle: String,
    val tag: String,
    val subtopics: List<Subtopic> = emptyList()
)

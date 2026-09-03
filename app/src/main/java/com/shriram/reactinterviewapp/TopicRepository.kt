package com.shriram.reactinterviewapp

import android.content.Context
import org.json.JSONArray

object TopicRepository {

    private var cachedTopics: List<Topic>? = null

    fun getAllTopics(context: Context): List<Topic> {
        cachedTopics?.let { return it }
        val loaded = loadFromAssets(context)
        cachedTopics = loaded
        return loaded
    }

    fun getTopicById(context: Context, id: Int): Topic? {
        return getAllTopics(context).find { it.id == id }
    }

    fun getSubtopic(context: Context, topicId: Int, subtopicId: Int): Subtopic? {
        val topic = getTopicById(context, topicId) ?: return null
        return topic.subtopics.find { it.id == subtopicId }
    }

    private fun loadFromAssets(context: Context): List<Topic> {
        try {
            // 1. Read questions.json
            val questionsJsonStr = readAssetFile(context, "questions.json")
            val questionsArray = JSONArray(questionsJsonStr)
            val questionsBySubtopic = mutableMapOf<Int, MutableList<QuestionItem>>()

            for (i in 0 until questionsArray.length()) {
                val qObj = questionsArray.getJSONObject(i)
                val subtopicId = qObj.getInt("subtopicId")
                val item = QuestionItem(
                    id = qObj.getInt("id"),
                    question = qObj.getString("question"),
                    answer = qObj.getString("answer")
                )
                questionsBySubtopic.getOrPut(subtopicId) { mutableListOf() }.add(item)
            }

            // 2. Read subtopics.json
            val subtopicsJsonStr = readAssetFile(context, "subtopics.json")
            val subtopicsArray = JSONArray(subtopicsJsonStr)
            val subtopicsByTopic = mutableMapOf<Int, MutableList<Subtopic>>()

            for (i in 0 until subtopicsArray.length()) {
                val sObj = subtopicsArray.getJSONObject(i)
                val subtopicId = sObj.getInt("id")
                val topicId = sObj.getInt("topicId")
                val subtopic = Subtopic(
                    id = subtopicId,
                    topicId = topicId,
                    title = sObj.getString("title"),
                    description = sObj.getString("description"),
                    questions = questionsBySubtopic[subtopicId] ?: emptyList()
                )
                subtopicsByTopic.getOrPut(topicId) { mutableListOf() }.add(subtopic)
            }

            // 3. Read topics.json
            val topicsJsonStr = readAssetFile(context, "topics.json")
            val topicsArray = JSONArray(topicsJsonStr)
            val topicsList = mutableListOf<Topic>()

            for (i in 0 until topicsArray.length()) {
                val tObj = topicsArray.getJSONObject(i)
                val topicId = tObj.getInt("id")
                val topic = Topic(
                    id = topicId,
                    title = tObj.getString("title"),
                    subtitle = tObj.getString("subtitle"),
                    tag = tObj.getString("tag"),
                    subtopics = subtopicsByTopic[topicId] ?: emptyList()
                )
                topicsList.add(topic)
            }

            return topicsList
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    private fun readAssetFile(context: Context, filename: String): String {
        return context.assets.open(filename).bufferedReader().use { it.readText() }
    }
}

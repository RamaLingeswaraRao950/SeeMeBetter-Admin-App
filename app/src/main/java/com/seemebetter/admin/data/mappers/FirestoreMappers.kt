package com.seemebetter.admin.data.mappers

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.seemebetter.admin.domain.model.Question
import com.seemebetter.admin.domain.model.QuestionType
import com.seemebetter.admin.domain.model.Response
import com.seemebetter.admin.domain.model.ResponseAnswer

fun DocumentSnapshot.toQuestion(): Question {
  val id = getString("id") ?: this.id
  return Question(
    id = id,
    title = getString("title") ?: "",
    description = getString("description") ?: "",
    type = QuestionType.fromWire(getString("type") ?: "text"),
    options = (get("options") as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
    required = getBoolean("required") ?: false,
    active = getBoolean("active") ?: true,
    isDeleted = getBoolean("isDeleted") ?: false,
    order = (getLong("order") ?: 0L),
    placeholder = getString("placeholder") ?: ""
  )
}

fun DocumentSnapshot.toResponse(): Response {
  val answersList = (get("answers") as? List<*>) ?: emptyList<Any>()
  val answers = answersList.mapNotNull { item ->
    val m = item as? Map<*, *> ?: return@mapNotNull null
    ResponseAnswer(
      questionId = m["questionId"] as? String ?: "",
      questionTitle = m["questionTitle"] as? String ?: "",
      type = m["type"] as? String ?: "",
      answer = m["answer"]
    )
  }
  return Response(
    id = this.id,
    createdAt = getTimestamp("createdAt"),
    anonymousId = getString("anonymousId") ?: "",
    source = getString("source") ?: "",
    answers = answers
  )
}


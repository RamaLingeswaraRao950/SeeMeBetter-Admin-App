package com.seemebetter.admin.domain.model

import com.google.firebase.Timestamp

data class ResponseAnswer(
  val questionId: String,
  val questionTitle: String,
  val type: String,
  val answer: Any?
)

data class Response(
  val id: String,
  val createdAt: Timestamp?,
  val anonymousId: String,
  val source: String,
  val answers: List<ResponseAnswer>
)


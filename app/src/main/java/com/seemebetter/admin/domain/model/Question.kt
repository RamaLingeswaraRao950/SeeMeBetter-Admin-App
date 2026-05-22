package com.seemebetter.admin.domain.model

enum class QuestionType(val wire: String) {
  Text("text"),
  Textarea("textarea"),
  McqSingle("mcq_single"),
  McqMultiple("mcq_multiple"),
  Rating1to5("rating_1_to_5");

  companion object {
    fun fromWire(value: String): QuestionType = entries.firstOrNull { it.wire == value } ?: Text
  }
}

data class Question(
  val id: String,
  val title: String,
  val description: String,
  val type: QuestionType,
  val options: List<String>,
  val required: Boolean,
  val active: Boolean,
  val isDeleted: Boolean,
  val order: Long,
  val placeholder: String
)


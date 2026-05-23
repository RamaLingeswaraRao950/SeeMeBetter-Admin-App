package com.seemebetter.admin.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.seemebetter.admin.data.mappers.toQuestion
import com.seemebetter.admin.domain.model.Question
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionsRepository @Inject constructor(
  private val db: FirebaseFirestore
) {
  data class QuestionsStream(
    val questions: List<Question> = emptyList(),
    val error: String? = null
  )

  fun observeQuestions(): Flow<QuestionsStream> = callbackFlow {
    val reg = db.collection("questions")
      .whereEqualTo("isDeleted", false)
      .addSnapshotListener { snap, err ->
        if (err != null) {
          trySend(QuestionsStream(error = err.message ?: "Failed to load questions"))
          return@addSnapshotListener
        }
        val data = (snap?.documents?.map { it.toQuestion() } ?: emptyList())
          .sortedBy { it.order }
        trySend(QuestionsStream(questions = data))
      }
    awaitClose { reg.remove() }
  }

  suspend fun createQuestion(input: Question) {
    val now = Timestamp.now()
    val doc = db.collection("questions").document()
    val payload = mapOf(
      "id" to doc.id,
      "title" to input.title,
      "description" to input.description,
      "type" to input.type.wire,
      "options" to input.options,
      "required" to input.required,
      "active" to input.active,
      "isDeleted" to false,
      "order" to input.order,
      "placeholder" to input.placeholder,
      "createdAt" to now,
      "updatedAt" to now
    )
    doc.set(payload).await()
  }

  suspend fun updateQuestion(id: String, patch: Map<String, Any?>) {
    db.collection("questions").document(id)
      .update(patch + mapOf("updatedAt" to Timestamp.now()))
      .await()
  }

  suspend fun softDeleteQuestion(id: String) {
    updateQuestion(id, mapOf("isDeleted" to true, "active" to false))
  }

  suspend fun reorder(orderedIds: List<String>) {
    val batch = db.batch()
    orderedIds.forEachIndexed { idx, id ->
      val ref = db.collection("questions").document(id)
      batch.update(ref, mapOf("order" to idx.toLong(), "updatedAt" to Timestamp.now()))
    }
    batch.commit().await()
  }
}

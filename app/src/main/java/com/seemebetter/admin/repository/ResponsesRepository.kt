package com.seemebetter.admin.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.seemebetter.admin.data.mappers.toResponse
import com.seemebetter.admin.domain.model.Response
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResponsesRepository @Inject constructor(
  private val auth: FirebaseAuth,
  private val db: FirebaseFirestore
) {
  suspend fun fetchRecent(limit: Long = 10): List<Response> {
    val uid = auth.currentUser?.uid ?: throw IllegalStateException("Not signed in")
    val snap = db.collection("users").document(uid).collection("responses")
      .orderBy("createdAt", Query.Direction.DESCENDING)
      .limit(limit)
      .get()
      .await()
    return snap.documents.map { it.toResponse() }
  }

  suspend fun fetchPage(afterCreatedAt: com.google.firebase.Timestamp?, limit: Long = 20): List<Response> {
    val uid = auth.currentUser?.uid ?: throw IllegalStateException("Not signed in")
    var q = db.collection("users").document(uid).collection("responses")
      .orderBy("createdAt", Query.Direction.DESCENDING)
      .limit(limit)
    if (afterCreatedAt != null) {
      q = q.startAfter(afterCreatedAt)
    }
    val snap = q.get().await()
    return snap.documents.map { it.toResponse() }
  }

  suspend fun getById(id: String): Response? {
    val uid = auth.currentUser?.uid ?: throw IllegalStateException("Not signed in")
    val snap = db.collection("users").document(uid).collection("responses").document(id).get().await()
    return if (snap.exists()) snap.toResponse() else null
  }

  suspend fun countTotal(): Long {
    val uid = auth.currentUser?.uid ?: throw IllegalStateException("Not signed in")
    val agg = db.collection("users").document(uid).collection("responses")
      .count()
      .get(com.google.firebase.firestore.AggregateSource.SERVER)
      .await()
    return agg.count
  }
}

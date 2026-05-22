package com.seemebetter.admin.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
  private val auth: FirebaseAuth,
  private val db: FirebaseFirestore
) {
  suspend fun requireAdmin(): Boolean {
    val uid = auth.currentUser?.uid ?: return false
    val snap = db.collection("admins").document(uid).get().await()
    val role = snap.getString("role") ?: ""
    return snap.exists() && role == "admin"
  }
}


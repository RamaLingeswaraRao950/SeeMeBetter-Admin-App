package com.seemebetter.admin.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class UserProfile(
  val uid: String,
  val handle: String,
  val profileName: String,
  val publicMessage: String,
  val cooldownHours: Long
)

@Singleton
class UserRepository @Inject constructor(
  private val auth: FirebaseAuth,
  private val db: FirebaseFirestore
) {
  suspend fun getMyProfile(): UserProfile? {
    val uid = auth.currentUser?.uid ?: return null
    val snap = db.collection("users").document(uid).get().await()
    if (!snap.exists()) return null
    val handle = snap.getString("handle") ?: ""
    return UserProfile(
      uid = uid,
      handle = handle,
      profileName = snap.getString("profileName") ?: "",
      publicMessage = snap.getString("publicMessage") ?: "Your response is anonymous and helps me improve.",
      cooldownHours = snap.getLong("cooldownHours") ?: 12L
    )
  }

  suspend fun reserveHandleAndCreateProfile(
    handleLowercase: String,
    profileName: String
  ) {
    val uid = auth.currentUser?.uid ?: throw IllegalStateException("Not signed in")
    val now = Timestamp.now()
    db.runTransaction { tx ->
      val handleRef = db.collection("handles").document(handleLowercase)
      val existing = tx.get(handleRef)
      if (existing.exists()) {
        throw IllegalStateException("Handle is already taken")
      }
      tx.set(
        handleRef,
        mapOf(
          "uid" to uid,
          "handle" to handleLowercase,
          "createdAt" to now
        )
      )
      val userRef = db.collection("users").document(uid)
      tx.set(
        userRef,
        mapOf(
          "uid" to uid,
          "handle" to handleLowercase,
          "profileName" to profileName,
          "publicMessage" to "Your response is anonymous and helps me improve.",
          "cooldownHours" to 12L,
          "createdAt" to now,
          "updatedAt" to now
        )
      )
      null
    }.await()
  }
}


package com.seemebetter.admin.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.seemebetter.admin.domain.model.GlobalSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
  private val auth: FirebaseAuth,
  private val db: FirebaseFirestore
) {
  suspend fun getGlobal(): GlobalSettings {
    val uid = auth.currentUser?.uid ?: throw IllegalStateException("Not signed in")
    val snap = db.collection("users").document(uid).get().await()
    val handle = snap.getString("handle") ?: ""
    val name = snap.getString("profileName") ?: ""
    val msg = snap.getString("publicMessage") ?: "Your response is anonymous and helps me improve."
    val cooldown = snap.getLong("cooldownHours") ?: 12L
    return GlobalSettings(handle = handle, profileName = name, publicMessage = msg, cooldownHours = cooldown)
  }

  suspend fun updateGlobal(profileName: String, publicMessage: String, cooldownHours: Long) {
    val uid = auth.currentUser?.uid ?: throw IllegalStateException("Not signed in")
    db.collection("users").document(uid)
      .set(
        mapOf(
          "uid" to uid,
          "profileName" to profileName,
          "publicMessage" to publicMessage,
          "cooldownHours" to cooldownHours,
          "updatedAt" to com.google.firebase.Timestamp.now()
        ),
        com.google.firebase.firestore.SetOptions.merge()
      )
      .await()
  }
}

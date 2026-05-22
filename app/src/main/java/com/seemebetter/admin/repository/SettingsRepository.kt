package com.seemebetter.admin.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seemebetter.admin.domain.model.GlobalSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
  private val db: FirebaseFirestore
) {
  suspend fun getGlobal(): GlobalSettings {
    val snap = db.collection("settings").document("global").get().await()
    val name = snap.getString("profileName") ?: ""
    val msg = snap.getString("publicMessage") ?: "Your response is anonymous and helps me improve."
    val cooldown = snap.getLong("cooldownHours") ?: 12L
    return GlobalSettings(profileName = name, publicMessage = msg, cooldownHours = cooldown)
  }

  suspend fun updateGlobal(profileName: String, publicMessage: String, cooldownHours: Long) {
    db.collection("settings").document("global")
      .set(mapOf("profileName" to profileName, "publicMessage" to publicMessage, "cooldownHours" to cooldownHours))
      .await()
  }
}

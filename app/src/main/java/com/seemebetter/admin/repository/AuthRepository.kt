package com.seemebetter.admin.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
  private val auth: FirebaseAuth
) {
  fun currentUser() = auth.currentUser

  fun observeLoggedIn(): Flow<Boolean> = callbackFlow {
    val listener = FirebaseAuth.AuthStateListener { fa ->
      trySend(fa.currentUser != null)
    }
    auth.addAuthStateListener(listener)
    trySend(auth.currentUser != null)
    awaitClose { auth.removeAuthStateListener(listener) }
  }

  suspend fun login(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password).await()
  }

  fun logout() {
    auth.signOut()
  }
}

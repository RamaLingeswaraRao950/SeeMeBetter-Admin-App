package com.seemebetter.admin.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seemebetter.admin.repository.AuthRepository
import com.seemebetter.admin.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SessionState(
  val isLoggedIn: Boolean,
  val handle: String? = null,
  val needsOnboarding: Boolean = false,
  val loading: Boolean = true
)

@HiltViewModel
class SessionViewModel @Inject constructor(
  private val authRepository: AuthRepository,
  private val userRepository: UserRepository
) : ViewModel() {
  private val _state = MutableStateFlow(
    SessionState(
      isLoggedIn = authRepository.currentUser() != null,
      loading = true
    )
  )
  val state: StateFlow<SessionState> = _state.asStateFlow()

  init {
    viewModelScope.launch {
      authRepository.observeLoggedIn().collect { loggedIn ->
        if (!loggedIn) {
          _state.value = SessionState(isLoggedIn = false, handle = null, needsOnboarding = false, loading = false)
        } else {
          _state.value = SessionState(isLoggedIn = true, handle = null, needsOnboarding = false, loading = true)
          val profile = try { userRepository.getMyProfile() } catch (_: Exception) { null }
          val handle = profile?.handle?.takeIf { it.isNotBlank() }
          _state.value = SessionState(isLoggedIn = true, handle = handle, needsOnboarding = handle == null, loading = false)
        }
      }
    }
  }
}

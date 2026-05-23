package com.seemebetter.admin.ui.onboarding

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

data class OnboardingState(
  val alreadyConfigured: Boolean = false,
  val saving: Boolean = false,
  val error: String? = null
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
  private val userRepository: UserRepository,
  private val authRepository: AuthRepository
) : ViewModel() {
  private val _state = MutableStateFlow(OnboardingState())
  val state: StateFlow<OnboardingState> = _state.asStateFlow()

  fun refresh() {
    viewModelScope.launch {
      val p = try { userRepository.getMyProfile() } catch (_: Exception) { null }
      val configured = p?.handle?.isNotBlank() == true
      if (configured) _state.value = OnboardingState(alreadyConfigured = true)
    }
  }

  fun save(handleInput: String, profileName: String, onDone: () -> Unit) {
    val handle = handleInput.trim().lowercase()
    if (!Regex("^[a-z0-9_-]{3,20}$").matches(handle)) {
      _state.value = OnboardingState(error = "Handle must be 3–20 chars: a-z 0-9 _ -")
      return
    }
    if (profileName.trim().isEmpty()) {
      _state.value = OnboardingState(error = "Please enter your name")
      return
    }
    _state.value = OnboardingState(saving = true)
    viewModelScope.launch {
      try {
        userRepository.reserveHandleAndCreateProfile(handleLowercase = handle, profileName = profileName.trim())
        _state.value = OnboardingState(saving = false)
        onDone()
      } catch (e: Exception) {
        _state.value = OnboardingState(saving = false, error = e.message ?: "Failed to save profile")
      }
    }
  }

  fun logout() {
    authRepository.logout()
  }
}

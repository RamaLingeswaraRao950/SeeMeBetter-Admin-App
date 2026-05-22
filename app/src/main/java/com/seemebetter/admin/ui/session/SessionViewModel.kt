package com.seemebetter.admin.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seemebetter.admin.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SessionState(val isLoggedIn: Boolean)

@HiltViewModel
class SessionViewModel @Inject constructor(
  private val authRepository: AuthRepository
) : ViewModel() {
  private val _state = MutableStateFlow(SessionState(isLoggedIn = authRepository.currentUser() != null))
  val state: StateFlow<SessionState> = _state.asStateFlow()

  init {
    viewModelScope.launch {
      authRepository.observeLoggedIn().collect { loggedIn ->
        _state.value = SessionState(isLoggedIn = loggedIn)
      }
    }
  }
}

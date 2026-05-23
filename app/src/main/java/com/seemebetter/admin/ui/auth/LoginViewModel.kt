package com.seemebetter.admin.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seemebetter.admin.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginUiState {
  data object Idle : LoginUiState
  data object Loading : LoginUiState
  data class Error(val message: String) : LoginUiState
  data object Success : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val authRepository: AuthRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
  val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

  fun login(email: String, password: String) {
    _uiState.value = LoginUiState.Loading
    viewModelScope.launch {
      try {
        authRepository.login(email.trim(), password)
        _uiState.value = LoginUiState.Success
      } catch (e: Exception) {
        _uiState.value = LoginUiState.Error(e.message ?: "Login failed")
      }
    }
  }

  fun signup(email: String, password: String) {
    _uiState.value = LoginUiState.Loading
    viewModelScope.launch {
      try {
        authRepository.createAccount(email.trim(), password)
        _uiState.value = LoginUiState.Success
      } catch (e: Exception) {
        _uiState.value = LoginUiState.Error(e.message ?: "Sign up failed")
      }
    }
  }
}

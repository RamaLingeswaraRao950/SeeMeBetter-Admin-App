package com.seemebetter.admin.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seemebetter.admin.domain.model.GlobalSettings
import com.seemebetter.admin.repository.AuthRepository
import com.seemebetter.admin.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
  val loading: Boolean = true,
  val settings: GlobalSettings? = null,
  val saving: Boolean = false,
  val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val repo: SettingsRepository,
  private val auth: AuthRepository
) : ViewModel() {
  private val _state = MutableStateFlow(SettingsState())
  val state: StateFlow<SettingsState> = _state.asStateFlow()

  fun load() {
    _state.value = SettingsState(loading = true)
    viewModelScope.launch {
      try {
        val s = repo.getGlobal()
        _state.value = SettingsState(loading = false, settings = s)
      } catch (e: Exception) {
        _state.value = SettingsState(loading = false, error = e.message ?: "Failed")
      }
    }
  }

  fun save(publicMessage: String, cooldownHours: Long) {
    _state.value = _state.value.copy(saving = true, error = null)
    viewModelScope.launch {
      try {
        val currentName = _state.value.settings?.profileName ?: ""
        repo.updateGlobal(currentName, publicMessage, cooldownHours)
        load()
      } catch (e: Exception) {
        _state.value = _state.value.copy(saving = false, error = e.message ?: "Failed")
      }
    }
  }

  fun saveAll(profileName: String, publicMessage: String, cooldownHours: Long) {
    _state.value = _state.value.copy(saving = true, error = null)
    viewModelScope.launch {
      try {
        repo.updateGlobal(profileName, publicMessage, cooldownHours)
        load()
      } catch (e: Exception) {
        _state.value = _state.value.copy(saving = false, error = e.message ?: "Failed")
      }
    }
  }

  fun logout() {
    auth.logout()
  }

  init {
    load()
  }
}

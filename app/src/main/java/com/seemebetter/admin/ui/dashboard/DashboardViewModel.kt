package com.seemebetter.admin.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seemebetter.admin.domain.model.Response
import com.seemebetter.admin.repository.ResponsesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardState(
  val loading: Boolean = true,
  val totalResponses: Long = 0,
  val recent: List<Response> = emptyList(),
  val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
  private val responsesRepository: ResponsesRepository
) : ViewModel() {
  private val _state = MutableStateFlow(DashboardState())
  val state: StateFlow<DashboardState> = _state.asStateFlow()

  fun refresh() {
    _state.value = _state.value.copy(loading = true, error = null)
    viewModelScope.launch {
      try {
        val total = responsesRepository.countTotal()
        val recent = responsesRepository.fetchRecent(10)
        _state.value = DashboardState(loading = false, totalResponses = total, recent = recent)
      } catch (e: Exception) {
        _state.value = DashboardState(loading = false, error = e.message ?: "Failed to load")
      }
    }
  }

  init {
    refresh()
  }
}


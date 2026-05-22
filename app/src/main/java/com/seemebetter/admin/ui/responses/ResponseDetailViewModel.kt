package com.seemebetter.admin.ui.responses

import androidx.lifecycle.SavedStateHandle
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

data class ResponseDetailState(
  val loading: Boolean = true,
  val response: Response? = null,
  val error: String? = null
)

@HiltViewModel
class ResponseDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val repo: ResponsesRepository
) : ViewModel() {
  private val responseId: String = savedStateHandle.get<String>("responseId") ?: ""
  private val _state = MutableStateFlow(ResponseDetailState())
  val state: StateFlow<ResponseDetailState> = _state.asStateFlow()

  init {
    viewModelScope.launch {
      try {
        val r = repo.getById(responseId)
        _state.value = ResponseDetailState(loading = false, response = r)
      } catch (e: Exception) {
        _state.value = ResponseDetailState(loading = false, error = e.message ?: "Failed")
      }
    }
  }
}


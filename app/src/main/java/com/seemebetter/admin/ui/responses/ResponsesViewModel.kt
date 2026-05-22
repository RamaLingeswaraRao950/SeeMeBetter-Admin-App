package com.seemebetter.admin.ui.responses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.seemebetter.admin.domain.model.Response
import com.seemebetter.admin.repository.ResponsesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResponsesState(
  val loading: Boolean = true,
  val items: List<Response> = emptyList(),
  val error: String? = null,
  val nextCursor: Timestamp? = null,
  val loadingMore: Boolean = false
)

@HiltViewModel
class ResponsesViewModel @Inject constructor(
  private val repo: ResponsesRepository
) : ViewModel() {
  private val _state = MutableStateFlow(ResponsesState())
  val state: StateFlow<ResponsesState> = _state.asStateFlow()

  fun refresh() {
    _state.value = ResponsesState(loading = true)
    viewModelScope.launch {
      try {
        val page = repo.fetchPage(afterCreatedAt = null, limit = 20)
        val cursor = page.lastOrNull()?.createdAt
        _state.value = ResponsesState(loading = false, items = page, nextCursor = cursor)
      } catch (e: Exception) {
        _state.value = ResponsesState(loading = false, error = e.message ?: "Failed")
      }
    }
  }

  fun loadMore() {
    val cur = _state.value
    if (cur.loadingMore || cur.nextCursor == null) return
    _state.value = cur.copy(loadingMore = true)
    viewModelScope.launch {
      try {
        val more = repo.fetchPage(afterCreatedAt = cur.nextCursor, limit = 20)
        val cursor = more.lastOrNull()?.createdAt
        _state.value = cur.copy(items = cur.items + more, nextCursor = cursor, loadingMore = false)
      } catch (e: Exception) {
        _state.value = cur.copy(error = e.message ?: "Failed", loadingMore = false)
      }
    }
  }

  init {
    refresh()
  }
}


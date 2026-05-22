package com.seemebetter.admin.ui.analytics

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

data class AnalyticsState(
  val loading: Boolean = true,
  val error: String? = null,
  val total: Long = 0,
  val ratingDistribution: Map<Int, Int> = emptyMap(),
  val mcqFrequencies: Map<String, Int> = emptyMap()
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
  private val repo: ResponsesRepository
) : ViewModel() {
  private val _state = MutableStateFlow(AnalyticsState())
  val state: StateFlow<AnalyticsState> = _state.asStateFlow()

  fun refresh() {
    _state.value = AnalyticsState(loading = true)
    viewModelScope.launch {
      try {
        val total = repo.countTotal()
        val recent = repo.fetchRecent(200)
        val rating = buildRatingDistribution(recent)
        val mcq = buildMcqFrequencies(recent)
        _state.value = AnalyticsState(loading = false, total = total, ratingDistribution = rating, mcqFrequencies = mcq)
      } catch (e: Exception) {
        _state.value = AnalyticsState(loading = false, error = e.message ?: "Failed")
      }
    }
  }

  private fun buildRatingDistribution(items: List<Response>): Map<Int, Int> {
    val map = mutableMapOf<Int, Int>()
    items.flatMap { it.answers }.forEach { a ->
      if (a.type == "rating_1_to_5") {
        val v = (a.answer as? Number)?.toInt() ?: return@forEach
        if (v in 1..5) map[v] = (map[v] ?: 0) + 1
      }
    }
    return map.toSortedMap()
  }

  private fun buildMcqFrequencies(items: List<Response>): Map<String, Int> {
    val map = mutableMapOf<String, Int>()
    items.flatMap { it.answers }.forEach { a ->
      when (a.type) {
        "mcq_single" -> {
          val v = a.answer as? String ?: return@forEach
          if (v.isNotBlank()) map[v] = (map[v] ?: 0) + 1
        }
        "mcq_multiple" -> {
          val list = a.answer as? List<*> ?: return@forEach
          list.mapNotNull { it as? String }.forEach { opt ->
            if (opt.isNotBlank()) map[opt] = (map[opt] ?: 0) + 1
          }
        }
      }
    }
    return map.entries.sortedByDescending { it.value }.associate { it.key to it.value }
  }

  init {
    refresh()
  }
}


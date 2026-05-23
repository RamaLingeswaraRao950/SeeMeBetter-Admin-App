package com.seemebetter.admin.ui.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seemebetter.admin.domain.model.Question
import com.seemebetter.admin.domain.model.QuestionType
import com.seemebetter.admin.repository.QuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuestionsUiState(
  val questions: List<Question> = emptyList(),
  val saving: Boolean = false,
  val error: String? = null
)

@HiltViewModel
class QuestionsViewModel @Inject constructor(
  private val repo: QuestionsRepository
) : ViewModel() {
  private val _ui = MutableStateFlow(QuestionsUiState())
  val ui: StateFlow<QuestionsUiState> = _ui.asStateFlow()

  init {
    viewModelScope.launch {
      repo.observeQuestions().collect { s ->
        _ui.value = _ui.value.copy(questions = s.questions, error = s.error)
      }
    }
  }

  fun create(
    title: String,
    description: String,
    type: QuestionType,
    required: Boolean,
    active: Boolean,
    order: Long,
    options: List<String>,
    placeholder: String
  ) {
    _ui.value = _ui.value.copy(saving = true, error = null)
    viewModelScope.launch {
      try {
        repo.createQuestion(
          Question(
            id = "",
            title = title,
            description = description,
            type = type,
            options = options,
            required = required,
            active = active,
            isDeleted = false,
            order = order,
            placeholder = placeholder
          )
        )
        _ui.value = _ui.value.copy(saving = false)
      } catch (e: Exception) {
        _ui.value = _ui.value.copy(saving = false, error = e.message ?: "Failed")
      }
    }
  }

  fun toggleActive(id: String, active: Boolean) = viewModelScope.launch {
    repo.updateQuestion(id, mapOf("active" to active))
  }

  fun update(id: String, patch: Map<String, Any?>) = viewModelScope.launch {
    repo.updateQuestion(id, patch)
  }

  fun softDelete(id: String) = viewModelScope.launch {
    repo.softDeleteQuestion(id)
  }

  fun reorder(ids: List<String>) = viewModelScope.launch {
    repo.reorder(ids)
  }
}

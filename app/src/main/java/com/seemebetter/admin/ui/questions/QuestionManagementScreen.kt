package com.seemebetter.admin.ui.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.seemebetter.admin.domain.model.Question
import com.seemebetter.admin.domain.model.QuestionType

@Composable
fun QuestionManagementScreen(
  viewModel: QuestionsViewModel = hiltViewModel()
) {
  val ui by viewModel.ui.collectAsState()
  val questions = ui.questions

  var showAdd by remember { mutableStateOf(false) }
  var editing by remember { mutableStateOf<Question?>(null) }

  Box(modifier = Modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
      if (ui.error != null) Text(ui.error ?: "", color = MaterialTheme.colorScheme.error)

      if (ui.error == null && questions.isEmpty()) {
        Card(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
          Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("No questions found", style = MaterialTheme.typography.titleMedium)
            Text(
              "If you created questions in Firebase Console, double-check you're using the same Firebase project and that your account is an admin.",
              style = MaterialTheme.typography.bodySmall
            )
          }
        }
      }

      LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(questions, key = { it.id }) { q ->
          QuestionRow(
            question = q,
            onToggleActive = { active -> viewModel.toggleActive(q.id, active) },
            onDelete = { viewModel.softDelete(q.id) },
            onEdit = { editing = q },
            onMoveUp = {
              val idx = questions.indexOfFirst { it.id == q.id }
              if (idx > 0) {
                val ids = questions.map { it.id }.toMutableList()
                val tmp = ids[idx - 1]
                ids[idx - 1] = ids[idx]
                ids[idx] = tmp
                viewModel.reorder(ids)
              }
            },
            onMoveDown = {
              val idx = questions.indexOfFirst { it.id == q.id }
              if (idx >= 0 && idx < questions.size - 1) {
                val ids = questions.map { it.id }.toMutableList()
                val tmp = ids[idx + 1]
                ids[idx + 1] = ids[idx]
                ids[idx] = tmp
                viewModel.reorder(ids)
              }
            }
          )
        }
        item { Spacer(modifier = Modifier.height(80.dp)) }
      }
    }

    FloatingActionButton(
      onClick = { showAdd = true },
      containerColor = MaterialTheme.colorScheme.primary,
      contentColor = MaterialTheme.colorScheme.onPrimary,
      elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      Icon(Icons.Outlined.Add, contentDescription = "Add question")
    }
  }

  if (showAdd) {
    AddQuestionDialog(
      onDismiss = { showAdd = false },
      onCreate = { input ->
        viewModel.create(
          title = input.title,
          description = input.description,
          type = input.type,
          required = input.required,
          active = input.active,
          order = questions.size.toLong(),
          options = input.options,
          placeholder = input.placeholder
        )
        showAdd = false
      }
    )
  }

  if (editing != null) {
    EditQuestionDialog(
      question = editing!!,
      onDismiss = { editing = null },
      onSave = { patch ->
        viewModel.update(editing!!.id, patch)
        editing = null
      }
    )
  }
}

@Composable
private fun QuestionRow(
  question: Question,
  onToggleActive: (Boolean) -> Unit,
  onEdit: () -> Unit,
  onDelete: () -> Unit,
  onMoveUp: () -> Unit,
  onMoveDown: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(question.title, style = MaterialTheme.typography.titleMedium)
        if (question.description.isNotBlank()) {
          Text(question.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(
          "${question.type.wire} • order ${question.order}${if (question.required) " • required" else ""}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Checkbox(checked = question.active, onCheckedChange = { onToggleActive(it) })
          Text("Active", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.weight(1f))

        IconButton(
          onClick = onMoveUp,
          colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) { Icon(Icons.Outlined.ArrowUpward, contentDescription = "Move up") }

        IconButton(
          onClick = onMoveDown,
          colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) { Icon(Icons.Outlined.ArrowDownward, contentDescription = "Move down") }

        IconButton(
          onClick = onEdit,
          colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) { Icon(Icons.Outlined.Edit, contentDescription = "Edit") }

        IconButton(
          onClick = onDelete,
          colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) { Icon(Icons.Outlined.DeleteOutline, contentDescription = "Delete") }
      }
    }
  }
}

data class AddQuestionInput(
  val title: String,
  val description: String,
  val type: QuestionType,
  val required: Boolean,
  val active: Boolean,
  val options: List<String>,
  val placeholder: String
)

@Composable
private fun AddQuestionDialog(
  onDismiss: () -> Unit,
  onCreate: (AddQuestionInput) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var placeholder by remember { mutableStateOf("") }
  var required by remember { mutableStateOf(true) }
  var active by remember { mutableStateOf(true) }
  var type by remember { mutableStateOf(QuestionType.Text) }
  var optionsRaw by remember { mutableStateOf("") }
  var typeMenu by remember { mutableStateOf(false) }

  androidx.compose.material3.AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(
        onClick = {
          val opts = optionsRaw.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
          onCreate(AddQuestionInput(title, description, type, required, active, opts, placeholder))
        },
        enabled = title.trim().isNotEmpty()
      ) { Text("Create") }
    },
    dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } },
    title = { Text("Add Question") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        OutlinedTextField(value = placeholder, onValueChange = { placeholder = it }, label = { Text("Placeholder") })
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Row {
            Checkbox(checked = required, onCheckedChange = { required = it })
            Text("Required")
          }
          Row {
            Checkbox(checked = active, onCheckedChange = { active = it })
            Text("Active")
          }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
          Button(onClick = { typeMenu = true }) { Text("Type: ${type.wire}") }
          DropdownMenu(expanded = typeMenu, onDismissRequest = { typeMenu = false }) {
            QuestionType.entries.forEach { t ->
              DropdownMenuItem(text = { Text(t.wire) }, onClick = { type = t; typeMenu = false })
            }
          }
        }
        if (type == QuestionType.McqSingle || type == QuestionType.McqMultiple) {
          OutlinedTextField(
            value = optionsRaw,
            onValueChange = { optionsRaw = it },
            label = { Text("Options (one per line)") },
            minLines = 3
          )
        }
      }
    }
  )
}

@Composable
private fun EditQuestionDialog(
  question: Question,
  onDismiss: () -> Unit,
  onSave: (Map<String, Any?>) -> Unit
) {
  var title by remember { mutableStateOf(question.title) }
  var description by remember { mutableStateOf(question.description) }
  var placeholder by remember { mutableStateOf(question.placeholder) }
  var required by remember { mutableStateOf(question.required) }
  var active by remember { mutableStateOf(question.active) }
  var type by remember { mutableStateOf(question.type) }
  var optionsRaw by remember { mutableStateOf(question.options.joinToString("\n")) }
  var typeMenu by remember { mutableStateOf(false) }

  androidx.compose.material3.AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(
        onClick = {
          val opts = optionsRaw.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
          onSave(
            mapOf(
              "title" to title,
              "description" to description,
              "placeholder" to placeholder,
              "required" to required,
              "active" to active,
              "type" to type.wire,
              "options" to opts
            )
          )
        },
        enabled = title.trim().isNotEmpty()
      ) { Text("Save") }
    },
    dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } },
    title = { Text("Edit Question") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        OutlinedTextField(value = placeholder, onValueChange = { placeholder = it }, label = { Text("Placeholder") })
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Row {
            Checkbox(checked = required, onCheckedChange = { required = it })
            Text("Required")
          }
          Row {
            Checkbox(checked = active, onCheckedChange = { active = it })
            Text("Active")
          }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
          Button(onClick = { typeMenu = true }) { Text("Type: ${type.wire}") }
          DropdownMenu(expanded = typeMenu, onDismissRequest = { typeMenu = false }) {
            QuestionType.entries.forEach { t ->
              DropdownMenuItem(text = { Text(t.wire) }, onClick = { type = t; typeMenu = false })
            }
          }
        }
        if (type == QuestionType.McqSingle || type == QuestionType.McqMultiple) {
          OutlinedTextField(
            value = optionsRaw,
            onValueChange = { optionsRaw = it },
            label = { Text("Options (one per line)") },
            minLines = 3
          )
        }
      }
    }
  )
}

package com.seemebetter.admin.ui.responses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ResponsesScreen(
  onBack: () -> Unit,
  onOpenDetail: (String) -> Unit,
  viewModel: ResponsesViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()

  Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      Button(onClick = onBack) { Text("Back") }
      Spacer(Modifier.weight(1f))
      Button(onClick = { viewModel.refresh() }) { Text("Refresh") }
    }
    Text("Responses", style = MaterialTheme.typography.headlineSmall)
    if (state.loading) {
      CircularProgressIndicator()
    } else if (state.error != null) {
      Text(state.error ?: "Error", color = MaterialTheme.colorScheme.error)
    } else {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
        items(state.items, key = { it.id }) { r ->
          Card(modifier = Modifier.fillMaxWidth().clickable { onOpenDetail(r.id) }) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text("ID: ${r.id}", style = MaterialTheme.typography.bodyMedium)
              Text("Answers: ${r.answers.size}", style = MaterialTheme.typography.bodySmall)
            }
          }
        }
        item {
          if (state.loadingMore) CircularProgressIndicator()
          else if (state.nextCursor != null) Button(onClick = { viewModel.loadMore() }) { Text("Load more") }
        }
      }
    }
  }
}


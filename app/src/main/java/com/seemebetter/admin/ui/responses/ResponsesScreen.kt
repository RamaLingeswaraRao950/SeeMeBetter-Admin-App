package com.seemebetter.admin.ui.responses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
@OptIn(ExperimentalMaterialApi::class)
fun ResponsesScreen(
  onOpenDetail: (String) -> Unit,
  viewModel: ResponsesViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()

  val pullState = rememberPullRefreshState(
    refreshing = state.loading,
    onRefresh = { viewModel.refresh() }
  )

  Box(modifier = Modifier.fillMaxSize().pullRefresh(pullState)) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      if (state.error != null) {
        Text(state.error ?: "Error", color = MaterialTheme.colorScheme.error)
      }

      if (!state.loading && state.items.isEmpty()) {
        Text("No responses yet.", style = MaterialTheme.typography.bodyMedium)
      } else {
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          items(state.items, key = { it.id }) { r ->
            Card(modifier = Modifier.fillMaxWidth().clickable { onOpenDetail(r.id) }) {
              Column(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Response", style = MaterialTheme.typography.titleMedium)
                Text(r.id, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Answers: ${r.answers.size}", style = MaterialTheme.typography.bodyMedium)
              }
            }
          }
          item {
            when {
              state.loadingMore -> CircularProgressIndicator()
              state.nextCursor != null -> Button(onClick = { viewModel.loadMore() }) { Text("Load more") }
            }
          }
        }
      }
    }

    PullRefreshIndicator(
      refreshing = state.loading,
      state = pullState,
      modifier = Modifier.align(androidx.compose.ui.Alignment.TopCenter)
    )
  }
}

package com.seemebetter.admin.ui.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
fun AnalyticsScreen(
  viewModel: AnalyticsViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()
  val pullState = rememberPullRefreshState(
    refreshing = state.loading,
    onRefresh = { viewModel.refresh() }
  )

  Box(modifier = Modifier.fillMaxSize().pullRefresh(pullState)) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      if (state.loading) {
        CircularProgressIndicator()
        return@Column
      }
      if (state.error != null) {
        Text(state.error ?: "Error", color = MaterialTheme.colorScheme.error)
        return@Column
      }

      Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text("Total responses", style = MaterialTheme.typography.titleMedium)
          Text("${state.total}", style = MaterialTheme.typography.headlineSmall)
        }
      }

      Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text("Rating distribution", style = MaterialTheme.typography.titleMedium)
          Text(state.ratingDistribution.entries.joinToString { "${it.key}: ${it.value}" })
        }
      }

      Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text("Top MCQ options", style = MaterialTheme.typography.titleMedium)
          Text(state.mcqFrequencies.entries.take(10).joinToString { "${it.key}: ${it.value}" })
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

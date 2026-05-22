package com.seemebetter.admin.ui.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun AnalyticsScreen(
  onBack: () -> Unit,
  viewModel: AnalyticsViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()
  Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Row {
      Button(onClick = onBack) { Text("Back") }
      Spacer(Modifier.weight(1f))
      Button(onClick = { viewModel.refresh() }) { Text("Refresh") }
    }
    Text("Analytics", style = MaterialTheme.typography.headlineSmall)

    if (state.loading) {
      CircularProgressIndicator()
      return
    }
    if (state.error != null) {
      Text(state.error ?: "Error", color = MaterialTheme.colorScheme.error)
      return
    }

    Card(modifier = Modifier.fillMaxWidth()) {
      Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Total responses: ${state.total}")
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
}


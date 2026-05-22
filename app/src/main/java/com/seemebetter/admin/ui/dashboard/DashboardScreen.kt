package com.seemebetter.admin.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
fun DashboardScreen(
  onOpenQuestions: () -> Unit,
  onOpenResponses: () -> Unit,
  onOpenAnalytics: () -> Unit,
  onOpenSettings: () -> Unit,
  viewModel: DashboardViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()

  Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text("Dashboard", style = MaterialTheme.typography.headlineSmall)

    Card(modifier = Modifier.padding(top = 4.dp)) {
      Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (state.loading) {
          Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CircularProgressIndicator(strokeWidth = 2.dp)
            Text("Loading…")
          }
        } else if (state.error != null) {
          Text(state.error ?: "Error", color = MaterialTheme.colorScheme.error)
        } else {
          Text("Total responses: ${state.totalResponses}")
          Text("Recent: ${state.recent.take(3).joinToString { it.id.take(6) }}")
        }
      }
    }

    Spacer(Modifier.height(4.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Button(onClick = onOpenQuestions) { Text("Questions") }
      Button(onClick = onOpenResponses) { Text("Responses") }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Button(onClick = onOpenAnalytics) { Text("Analytics") }
      Button(onClick = onOpenSettings) { Text("Settings") }
    }
  }
}


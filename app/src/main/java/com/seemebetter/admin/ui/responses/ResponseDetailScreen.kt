package com.seemebetter.admin.ui.responses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ResponseDetailScreen(
  onBack: () -> Unit,
  viewModel: ResponseDetailViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Response") },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
          }
        }
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      if (state.loading) {
        CircularProgressIndicator()
      } else if (state.error != null) {
        Text(state.error ?: "Error", color = MaterialTheme.colorScheme.error)
      } else if (state.response == null) {
        Text("Not found.")
      } else {
        val r = state.response!!
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
          items(r.answers) { a ->
            Card(modifier = Modifier.fillMaxWidth()) {
              Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(a.questionTitle, style = MaterialTheme.typography.titleSmall)
                Text(a.answer?.toString() ?: "", style = MaterialTheme.typography.bodyMedium)
              }
            }
          }
        }
      }
    }
  }
}

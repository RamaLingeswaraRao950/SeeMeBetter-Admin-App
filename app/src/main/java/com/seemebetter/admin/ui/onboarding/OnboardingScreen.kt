package com.seemebetter.admin.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OnboardingScreen(
  onDone: () -> Unit,
  onLoggedOut: () -> Unit,
  viewModel: OnboardingViewModel = hiltViewModel()
) {
  val state by viewModel.state.collectAsState()
  var handle by remember { mutableStateOf("") }
  var name by remember { mutableStateOf("") }

  LaunchedEffect(Unit) {
    viewModel.refresh()
  }

  LaunchedEffect(state.alreadyConfigured) {
    if (state.alreadyConfigured) onDone()
  }

  Column(
    modifier = Modifier.fillMaxSize().padding(20.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("Set up your public link", style = MaterialTheme.typography.headlineSmall)
    Text("Choose a handle for your feedback form", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.height(18.dp))

    OutlinedTextField(
      value = name,
      onValueChange = { name = it },
      label = { Text("Your name") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true
    )
    Spacer(Modifier.height(12.dp))
    OutlinedTextField(
      value = handle,
      onValueChange = { handle = it },
      label = { Text("Handle (e.g. ramalingam)") },
      modifier = Modifier.fillMaxWidth(),
      supportingText = { Text("Allowed: a-z 0-9 _ - (3–20 chars). Lowercase only.") },
      singleLine = true
    )

    Spacer(Modifier.height(16.dp))
    Button(
      onClick = { viewModel.save(handle, name, onDone) },
      enabled = !state.saving,
      modifier = Modifier.fillMaxWidth()
    ) {
      if (state.saving) CircularProgressIndicator(strokeWidth = 2.dp)
      else Text("Create my link")
    }

    if (state.error != null) {
      Spacer(Modifier.height(10.dp))
      Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
    }

    Spacer(Modifier.height(10.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
      TextButton(
        onClick = {
          viewModel.logout()
          onLoggedOut()
        },
        enabled = !state.saving
      ) { Text("Logout") }
    }
  }
}

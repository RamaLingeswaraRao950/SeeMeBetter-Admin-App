package com.seemebetter.admin.ui.settings

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
  onBack: () -> Unit,
  onLoggedOut: () -> Unit,
  viewModel: SettingsViewModel = hiltViewModel()
) {
  val ctx = LocalContext.current
  val state by viewModel.state.collectAsState()
  var profileName by remember { mutableStateOf("") }
  var message by remember { mutableStateOf("") }
  var cooldown by remember { mutableStateOf("12") }

  Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Row {
      Button(onClick = onBack) { Text("Back") }
      Spacer(Modifier.weight(1f))
    }
    Text("Settings", style = MaterialTheme.typography.headlineSmall)

    if (state.loading) {
      CircularProgressIndicator()
      return
    }
    if (state.error != null) Text(state.error ?: "", color = MaterialTheme.colorScheme.error)

    val s = state.settings
    if (s != null && message.isBlank() && profileName.isBlank()) {
      profileName = s.profileName
      message = s.publicMessage
      cooldown = s.cooldownHours.toString()
    }

    OutlinedTextField(
      value = profileName,
      onValueChange = { profileName = it },
      label = { Text("Profile name") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true
    )
    OutlinedTextField(
      value = message,
      onValueChange = { message = it },
      label = { Text("Public message") },
      modifier = Modifier.fillMaxWidth(),
      minLines = 2
    )
    OutlinedTextField(
      value = cooldown,
      onValueChange = { cooldown = it },
      label = { Text("Cooldown hours") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true
    )

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Button(
        onClick = { viewModel.saveAll(profileName, message, cooldown.toLongOrNull() ?: 12L) },
        enabled = !state.saving
      ) { Text(if (state.saving) "Saving…" else "Save") }

      Button(
        onClick = {
          val share = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "SeeMeBetter export (manual)")
            putExtra(Intent.EXTRA_TEXT, "Export is available in the next iteration.")
          }
          ctx.startActivity(Intent.createChooser(share, "Share"))
        }
      ) { Text("Export") }

      Button(
        onClick = {
          viewModel.logout()
          onLoggedOut()
        }
      ) { Text("Logout") }
    }
  }
}

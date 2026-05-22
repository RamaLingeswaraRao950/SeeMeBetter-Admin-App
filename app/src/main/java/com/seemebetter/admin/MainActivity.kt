package com.seemebetter.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.seemebetter.admin.ui.theme.SeeMeBetterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SeeMeBetterTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
          SeeMeBetterApp()
        }
      }
    }
  }
}


package com.seemebetter.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.seemebetter.admin.navigation.AdminNavHost
import com.seemebetter.admin.ui.session.SessionViewModel

@Composable
fun SeeMeBetterApp(sessionViewModel: SessionViewModel = hiltViewModel()) {
  val session by sessionViewModel.state.collectAsState()
  AdminNavHost(isLoggedIn = session.isLoggedIn)
}


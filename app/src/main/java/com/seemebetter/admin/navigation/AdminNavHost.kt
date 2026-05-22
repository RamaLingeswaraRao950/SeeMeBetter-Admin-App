package com.seemebetter.admin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.seemebetter.admin.ui.analytics.AnalyticsScreen
import com.seemebetter.admin.ui.auth.LoginScreen
import com.seemebetter.admin.ui.questions.QuestionManagementScreen
import com.seemebetter.admin.ui.responses.ResponseDetailScreen
import com.seemebetter.admin.ui.responses.ResponsesScreen
import com.seemebetter.admin.ui.settings.SettingsScreen
import com.seemebetter.admin.ui.shell.AdminMainScaffold

@Composable
fun AdminNavHost(isLoggedIn: Boolean) {
  val nav = rememberNavController()
  val start = if (isLoggedIn) Routes.Main else Routes.Login

  NavHost(navController = nav, startDestination = start) {
    composable(Routes.Login) {
      LoginScreen(
        onLoggedIn = {
          nav.navigate(Routes.Main) { popUpTo(Routes.Login) { inclusive = true } }
        }
      )
    }
    composable(Routes.Main) {
      AdminMainScaffold(
        onOpenResponseDetail = { id -> nav.navigate("${Routes.ResponseDetail}/$id") },
        onLoggedOut = { nav.navigate(Routes.Login) { popUpTo(0) } }
      )
    }
    composable(
      route = "${Routes.ResponseDetail}/{responseId}",
      arguments = listOf(navArgument("responseId") { type = NavType.StringType })
    ) {
      ResponseDetailScreen(onBack = { nav.popBackStack() })
    }
  }
}

package com.seemebetter.admin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.seemebetter.admin.ui.analytics.AnalyticsScreen
import com.seemebetter.admin.ui.auth.LoginScreen
import com.seemebetter.admin.ui.dashboard.DashboardScreen
import com.seemebetter.admin.ui.questions.QuestionManagementScreen
import com.seemebetter.admin.ui.responses.ResponseDetailScreen
import com.seemebetter.admin.ui.responses.ResponsesScreen
import com.seemebetter.admin.ui.settings.SettingsScreen

@Composable
fun AdminNavHost(isLoggedIn: Boolean) {
  val nav = rememberNavController()
  val start = if (isLoggedIn) Routes.Dashboard else Routes.Login

  NavHost(navController = nav, startDestination = start) {
    composable(Routes.Login) {
      LoginScreen(
        onLoggedIn = {
          nav.navigate(Routes.Dashboard) { popUpTo(Routes.Login) { inclusive = true } }
        }
      )
    }
    composable(Routes.Dashboard) {
      DashboardScreen(
        onOpenQuestions = { nav.navigate(Routes.Questions) },
        onOpenResponses = { nav.navigate(Routes.Responses) },
        onOpenAnalytics = { nav.navigate(Routes.Analytics) },
        onOpenSettings = { nav.navigate(Routes.Settings) }
      )
    }
    composable(Routes.Questions) { QuestionManagementScreen(onBack = { nav.popBackStack() }) }
    composable(Routes.Responses) {
      ResponsesScreen(
        onBack = { nav.popBackStack() },
        onOpenDetail = { id -> nav.navigate("${Routes.ResponseDetail}/$id") }
      )
    }
    composable(
      route = "${Routes.ResponseDetail}/{responseId}",
      arguments = listOf(navArgument("responseId") { type = NavType.StringType })
    ) {
      ResponseDetailScreen(onBack = { nav.popBackStack() })
    }
    composable(Routes.Analytics) { AnalyticsScreen(onBack = { nav.popBackStack() }) }
    composable(Routes.Settings) { SettingsScreen(onBack = { nav.popBackStack() }, onLoggedOut = {
      nav.navigate(Routes.Login) { popUpTo(0) }
    }) }
  }
}


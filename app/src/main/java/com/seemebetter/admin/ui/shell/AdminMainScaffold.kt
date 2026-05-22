package com.seemebetter.admin.ui.shell

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.seemebetter.admin.navigation.AdminBottomTabs
import com.seemebetter.admin.navigation.BottomTab
import com.seemebetter.admin.navigation.Routes
import com.seemebetter.admin.ui.analytics.AnalyticsScreen
import com.seemebetter.admin.ui.questions.QuestionManagementScreen
import com.seemebetter.admin.ui.responses.ResponsesScreen
import com.seemebetter.admin.ui.settings.SettingsScreen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AdminMainScaffold(
  onOpenResponseDetail: (String) -> Unit,
  onLoggedOut: () -> Unit,
  modifier: Modifier = Modifier
) {
  val tabNav = rememberNavController()
  val backStackEntry by tabNav.currentBackStackEntryAsState()
  val currentRoute = backStackEntry?.destination?.route

  val title = when (currentRoute) {
    Routes.Questions -> "Questions"
    Routes.Analytics -> "Analytics"
    Routes.Settings -> "Settings"
    else -> "Responses"
  }

  Scaffold(
    modifier = modifier,
    topBar = { TopAppBar(title = { Text(title) }) },
    bottomBar = {
      NavigationBar {
        AdminBottomTabs.forEach { tab ->
          val selected = currentRoute == tab.route || (currentRoute == null && tab is BottomTab.Responses)
          NavigationBarItem(
            selected = selected,
            onClick = {
              tabNav.navigate(tab.route) {
                popUpTo(tabNav.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
              }
            },
            icon = { Icon(tab.icon, contentDescription = tab.label) },
            label = { Text(tab.label) }
          )
        }
      }
    }
  ) { padding ->
    NavHost(
      navController = tabNav,
      startDestination = Routes.Responses,
      modifier = Modifier
        .padding(padding)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      composable(Routes.Responses) { ResponsesScreen(onOpenDetail = onOpenResponseDetail) }
      composable(Routes.Questions) { QuestionManagementScreen() }
      composable(Routes.Analytics) { AnalyticsScreen() }
      composable(Routes.Settings) { SettingsScreen(onLoggedOut = onLoggedOut) }
    }
  }
}

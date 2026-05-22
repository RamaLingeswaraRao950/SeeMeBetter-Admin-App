package com.seemebetter.admin.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomTab(
  val route: String,
  val label: String,
  val icon: ImageVector
) {
  data object Responses : BottomTab(Routes.Responses, "Responses", Icons.Outlined.ListAlt)
  data object Questions : BottomTab(Routes.Questions, "Questions", Icons.Outlined.QuestionAnswer)
  data object Analytics : BottomTab(Routes.Analytics, "Analytics", Icons.Outlined.Analytics)
  data object Settings : BottomTab(Routes.Settings, "Settings", Icons.Outlined.Settings)
}

val AdminBottomTabs: List<BottomTab> = listOf(
  BottomTab.Responses,
  BottomTab.Questions,
  BottomTab.Analytics,
  BottomTab.Settings
)


package com.seemebetter.admin.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
  primary = SeeMeBetterColors.Orange,
  onPrimary = Color(0xFF1B1B1F),
  primaryContainer = Color(0xFFFFD9B3),
  onPrimaryContainer = Color(0xFF2A1700),
  secondary = Color(0xFF5A5D72),
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFE0E1EA),
  onSecondaryContainer = Color(0xFF171A2C),
  background = Color.White,
  onBackground = Color(0xFF1B1B1F),
  surface = SeeMeBetterColors.SurfaceLight,
  onSurface = Color(0xFF1B1B1F),
  surfaceVariant = Color(0xFFE7E0EC),
  onSurfaceVariant = Color(0xFF49454F),
  outline = Color(0xFF7A757F),
  error = Color(0xFFBA1A1A),
  onError = Color.White
)

private val DarkColors = darkColorScheme(
  primary = SeeMeBetterColors.OrangeDark,
  onPrimary = Color(0xFF1B1B1F),
  primaryContainer = Color(0xFF5C3A00),
  onPrimaryContainer = Color(0xFFFFDDB7),
  secondary = Color(0xFFC3C5DD),
  onSecondary = Color(0xFF2D3042),
  secondaryContainer = Color(0xFF44475A),
  onSecondaryContainer = Color(0xFFE1E2F1),
  background = SeeMeBetterColors.SurfaceDark,
  onBackground = Color(0xFFE5E1E6),
  surface = SeeMeBetterColors.SurfaceDark,
  onSurface = Color(0xFFE5E1E6),
  surfaceVariant = Color(0xFF49454F),
  onSurfaceVariant = Color(0xFFCAC4D0),
  outline = Color(0xFF948F99),
  error = Color(0xFFFFB4AB),
  onError = Color(0xFF690005)
)

@Composable
fun SeeMeBetterTheme(content: @Composable () -> Unit) {
  val dark = isSystemInDarkTheme()
  MaterialTheme(
    colorScheme = if (dark) DarkColors else LightColors,
    typography = Typography(),
    content = content
  )
}

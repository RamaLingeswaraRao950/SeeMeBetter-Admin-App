package com.seemebetter.admin.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.seemebetter.admin.R

@Composable
fun LoginScreen(
  onLoggedIn: () -> Unit,
  viewModel: LoginViewModel = hiltViewModel()
) {
  val state by viewModel.uiState.collectAsState()
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }
  var isSignup by remember { mutableStateOf(false) }

  if (state is LoginUiState.Success) {
    onLoggedIn()
  }

  Column(
    modifier = Modifier.fillMaxSize().padding(20.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
//    Image(
//      painter = painterResource(id = R.drawable.app_logo),
//      contentDescription = "SeeMeBetter logo",
//      modifier = Modifier.size(width = 220.dp, height = 140.dp),
//      contentScale = ContentScale.Fit
//    )
    Text(if (isSignup) "Create account" else "Login", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
    Spacer(Modifier.height(20.dp))
    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(12.dp))
    OutlinedTextField(
      value = password,
      onValueChange = { password = it },
      label = { Text("Password") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
      visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
      trailingIcon = {
        IconButton(onClick = { passwordVisible = !passwordVisible }) {
          Icon(
            imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
            contentDescription = if (passwordVisible) "Hide password" else "Show password"
          )
        }
      }
    )
    Spacer(Modifier.height(16.dp))
    Button(
      onClick = { if (isSignup) viewModel.signup(email, password) else viewModel.login(email, password) },
      enabled = state !is LoginUiState.Loading,
      modifier = Modifier.fillMaxWidth()
    ) {
      if (state is LoginUiState.Loading) CircularProgressIndicator(strokeWidth = 2.dp)
      else Text(if (isSignup) "Create account" else "Login")
    }
    Spacer(Modifier.height(8.dp))
    TextButton(
      onClick = { isSignup = !isSignup },
      enabled = state !is LoginUiState.Loading
    ) {
      Text(if (isSignup) "I already have an account" else "Create an account")
    }
    if (state is LoginUiState.Error) {
      Spacer(Modifier.height(10.dp))
      Text((state as LoginUiState.Error).message, color = MaterialTheme.colorScheme.error)
    }
  }
}

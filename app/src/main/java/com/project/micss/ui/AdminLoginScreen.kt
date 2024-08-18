package com.project.micss.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AdminLoginScreen(viewModel: AdminLoginViewModel = viewModel(), onBack: () -> Unit,
                     onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var attemptSubmit by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authResult by viewModel.authResult.collectAsState()

    LaunchedEffect(authResult) {
        authResult?.let { result ->
            if (result) {
                Toast.makeText(context, "Admin login successful", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            } else {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                Log.e("AdminLogin", "Invalid credentials provided")
            }
            isLoading = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Admin Portal",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        leadingIcon = { Icon(imageVector = Icons.Default.PersonOutline, contentDescription = "Email Icon") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = attemptSubmit && username.isEmpty()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon") },
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Default.Visibility
                            else Icons.Default.VisibilityOff

                            IconButton(onClick = {
                                passwordVisible = !passwordVisible
                            }) {
                                Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        isError = attemptSubmit && password.isEmpty()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            attemptSubmit = true
                            isLoading = true
                            if (username.isNotEmpty() && password.isNotEmpty()) {
                                viewModel.loginAdmin(username, password)
                            } else {
                                isLoading = false
                                Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show()
                                Log.e("AdminLogin", "username or password is empty")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color(220, 87, 45, 242)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Login")
                    }
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
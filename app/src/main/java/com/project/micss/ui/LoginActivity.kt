package com.project.micss.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.project.micss.LoginViewModel
import com.project.micss.R

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContent {
            MaterialTheme {
                LoginScreen(auth)
            }
        }
    }
}

enum class LoginState {
    LOGIN, SIGN_UP
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(auth: FirebaseAuth, viewModel: LoginViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var attemptSubmit by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authResult by viewModel.authResult.collectAsState()
    var loginState by remember { mutableStateOf(LoginState.LOGIN) }

    LaunchedEffect(authResult) {
        authResult?.let { result ->
            handleAuthResult(result, context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 12.dp, top = 0.dp, end = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.user_login), // Replace with your icon resource
            contentDescription = "Login Icon",
            modifier = Modifier.size(220.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email TextField with Error Handling
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    text = "Email" + if (attemptSubmit && email.isEmpty()) " is required" else "",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (attemptSubmit && email.isEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Password TextField with Error Handling
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    text = "Password" + if (attemptSubmit && password.isEmpty()) " is required" else "",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (attemptSubmit && password.isEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Conditional Fields for Sign Up
        if (loginState == LoginState.SIGN_UP) {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = {
                    Text(
                        text = "Phone Number" + if (attemptSubmit && phoneNumber.isEmpty()) " is required" else "",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = if (attemptSubmit && phoneNumber.isEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Row for District and Area
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = district,
                    onValueChange = { district = it },
                    label = {
                        Text(
                            text = "District" + if (attemptSubmit && district.isEmpty()) " is required" else "",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (attemptSubmit && district.isEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = {
                        Text(
                            text = "Area" + if (attemptSubmit && area.isEmpty()) " is required" else "",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (attemptSubmit && area.isEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Row for Sex and Age
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = sex,
                    onValueChange = { sex = it },
                    label = {
                        Text(
                            text = "Sex" + if (attemptSubmit && sex.isEmpty()) " is required" else "",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (attemptSubmit && sex.isEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = {
                        Text(
                            text = "Age" + if (attemptSubmit && age.isEmpty()) " is required" else "",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (attemptSubmit && age.isEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Login or Sign Up Button
        Button(
            onClick = {
                attemptSubmit = true
                if (loginState == LoginState.LOGIN) {
                    if (isValidEmail(email) && password.isNotEmpty()) {
                        viewModel.loginUser(auth, email, password)
                    } else {
                        Toast.makeText(context, "Please enter valid email and password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (isValidEmail(email) && password.isNotEmpty() &&
                        phoneNumber.isNotEmpty() && district.isNotEmpty() &&
                        area.isNotEmpty() && sex.isNotEmpty() && age.isNotEmpty()
                    ) {
                        viewModel.signUpUser(auth, email, password, phoneNumber, district, area, sex, age)
                    } else {
                        Toast.makeText(context, "Please fill all required fields correctly", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(220, 87, 45, 242)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (loginState == LoginState.LOGIN) "Login" else "Sign Up")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle Login/Sign Up Text
        ClickableText(
            text = AnnotatedString(if (loginState == LoginState.LOGIN) "Not a user? Sign up" else "Already have an account? Login"),
            onClick = {
                attemptSubmit = false
                loginState = if (loginState == LoginState.LOGIN) LoginState.SIGN_UP else LoginState.LOGIN
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge.copy(color = Color(63, 81, 181, 226))
        )
    }
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun handleAuthResult(result: Result<Unit>, context: Context) {
    if (result.isSuccess) {
        Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
        // Navigate to main activity
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    } else {
        val errorMessage = when (result.exceptionOrNull()) {
            is FirebaseAuthInvalidUserException -> "Account not found"
            is FirebaseAuthInvalidCredentialsException -> "Wrong email or password"
            else -> "Authentication failed: ${result.exceptionOrNull()?.localizedMessage}"
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}

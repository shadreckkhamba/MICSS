package com.project.micss.ui

import NoInternetScreen
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.project.micss.LoginViewModel
import com.project.micss.R
import kotlinx.coroutines.delay

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContent {
            MaterialTheme {
                NetworkAndContentScreen(auth)
            }
        }
    }
}

@Composable
fun NetworkAndContentScreen(auth: FirebaseAuth) {
    var isNetworkAvailable by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var loginState by remember { mutableStateOf(LoginState.LOGIN) }

    val context = LocalContext.current
    val adminLoginViewModel: AdminLoginViewModel = viewModel()

    // Check network availability
    LaunchedEffect(Unit) {
        // Simulate network check
        isNetworkAvailable = NetworkUtils.isNetworkAvailable(context)
        delay(4000) // Simulate loading time
        isLoading = false
    }

    if (isLoading) {
        LoadingScreen()
    } else if (isNetworkAvailable) {
        when (loginState) {
            LoginState.LOGIN -> LoginScreen(auth, navigateToAdminLogin = { loginState = LoginState.ADMIN_LOGIN })
            LoginState.ADMIN_LOGIN -> AdminLoginScreen(
                viewModel = adminLoginViewModel,
                onBack = {
                    adminLoginViewModel.resetAuthResult() // Reset auth result when navigating back
                    loginState = LoginState.LOGIN
                },
                onLoginSuccess = { loginState = LoginState.ADMIN_PORTAL }
            )
            LoginState.ADMIN_PORTAL -> AdminPortalScreen(
                onLogout = {
                    adminLoginViewModel.logoutAdmin() // Clear credentials and reset auth state
                    loginState = LoginState.ADMIN_LOGIN
                }
            )
            else -> NoInternetScreen()
        }
    } else {
        NoInternetScreen()
    }
}



@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(220, 87, 45, 242)), // Background color
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MICSS",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp)) // Spacing between text and indicator
            Text(
                text = "Mobile Integrated Counseling Support System",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(24.dp)) // Spacing between text and loading indicator
            CircularProgressIndicator(
                color = Color.White // Loading symbol color
            )
        }
    }
}

@Composable
fun LoginScreen(auth: FirebaseAuth, navigateToAdminLogin: () -> Unit, viewModel: LoginViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var attemptSubmit by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Client") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authResult by viewModel.authResult.collectAsState()
    var loginState by remember { mutableStateOf(LoginState.LOGIN) }

    LaunchedEffect(authResult) {
        authResult?.let { result ->
            handleAuthResult(result, context, auth.currentUser?.uid ?: "")
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 12.dp, top = 24.dp, end = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Card for Login Form
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = if (loginState == LoginState.LOGIN) "Login" else "Sign Up",
                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.Black), // Larger font size and black color
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally) // Center alignment
                        .padding(bottom = 8.dp) // Optional padding for spacing
                        .padding(top = 8.dp)
                )


                Spacer(modifier = Modifier.height(8.dp))

                // Icon
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.user_login),
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

                // Password TextField with Error Handling and Toggle Visibility
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
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
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

                    // Row for District and Area, to manage the window space
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
                        isLoading = true
                        if (loginState == LoginState.LOGIN) {
                            if (isValidEmail(email) && password.isNotEmpty()) {
                                viewModel.loginUser(auth, email, password)
                            } else {
                                Toast.makeText(context, "Please enter valid email and password", Toast.LENGTH_SHORT).show()
                                isLoading = false
                            }
                        } else {
                            if (isValidEmail(email) && password.isNotEmpty() &&
                                phoneNumber.isNotEmpty() && district.isNotEmpty() &&
                                area.isNotEmpty() && sex.isNotEmpty() && age.isNotEmpty()
                            ) {
                                viewModel.signUpUser(auth, email, password, phoneNumber, district, area, sex, age)
                            } else {
                                Toast.makeText(context, "Please fill all required fields correctly", Toast.LENGTH_SHORT).show()
                                isLoading = false
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

                // Loading Indicator
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(220, 87, 45)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Toggle Login/Sign Up Text
                ClickableText(
                    text = AnnotatedString(if (loginState == LoginState.LOGIN) "Don't have account? Sign up" else "Already have account? Login"),
                    onClick = {
                        attemptSubmit = false
                        loginState = if (loginState == LoginState.LOGIN) LoginState.SIGN_UP else LoginState.LOGIN
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color(200, 87, 45, 255))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loginState == LoginState.LOGIN) {
            ClickableText(
                text = AnnotatedString("Admin Portal"),
                onClick = { navigateToAdminLogin() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(color = Color(0, 188, 212, 255))
            )
        }
    }
}


fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun handleAuthResult(result: Result<Unit>, context: Context, userId: String) {
    if (result.isSuccess) {
        Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
        // Navigate to main activity
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("userId", userId) // Pass userId to MainActivity
        }
        context.startActivity(intent)
        (context as Activity).finish() // Finish LoginActivity
    } else {
        val errorMessage = when (result.exceptionOrNull()) {
            is FirebaseAuthInvalidUserException -> "Account not found"
            is FirebaseAuthInvalidCredentialsException -> "Wrong email or password"
            else -> "Authentication failed: ${result.exceptionOrNull()?.localizedMessage}"
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
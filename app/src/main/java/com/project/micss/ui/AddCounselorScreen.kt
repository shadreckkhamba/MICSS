package com.project.micss.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCounselorScreen(
    viewModel: CounselorsContentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onAddCounselor: () -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var expertiseList = remember { mutableStateListOf("") }
    var availabilityList = remember { mutableStateListOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Display snackbar message if errorMessage or successMessage is not null
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            errorMessage = null
        }
    }

    LaunchedEffect(successMessage) {
        successMessage?.let {
            snackbarHostState.showSnackbar(it)
            successMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Counselor") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Title
                    Text(
                        text = "Counselor Details",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Name Field
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Name Icon") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = name.isBlank() && isSubmitting
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Expertise Fields
                    Text("Expertise")
                    expertiseList.forEachIndexed { index, expertise ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 8.dp)
                            ) {
                                OutlinedTextField(
                                    value = expertise,
                                    onValueChange = { expertiseList[index] = it },
                                    label = { Text("Expertise ${index + 1}") },
                                    leadingIcon = { Icon(imageVector = Icons.Default.Build, contentDescription = "Expertise Icon") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = expertise.isBlank() && isSubmitting
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                IconButton(
                                    onClick = { if (expertiseList.size > 1) expertiseList.removeAt(index) },
                                    enabled = expertiseList.size > 1,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Filled.Remove, contentDescription = "Remove Expertise")
                                }
                                IconButton(
                                    onClick = { expertiseList.add("") },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = "Add Expertise")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Availability Fields
                    Text("Availability")
                    availabilityList.forEachIndexed { index, availability ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 8.dp)
                            ) {
                                OutlinedTextField(
                                    value = availability,
                                    onValueChange = { availabilityList[index] = it },
                                    label = { Text("Availability ${index + 1}") },
                                    leadingIcon = { Icon(imageVector = Icons.Default.Schedule, contentDescription = "Availability Icon") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = availability.isBlank() && isSubmitting
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                IconButton(
                                    onClick = { if (availabilityList.size > 1) availabilityList.removeAt(index) },
                                    enabled = availabilityList.size > 1,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Filled.Remove, contentDescription = "Remove Availability")
                                }
                                IconButton(
                                    onClick = { availabilityList.add("") },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = "Add Availability")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && isSubmitting
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Phone Field
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone Icon") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = phone.length < 10 && isSubmitting
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (!isSubmitting) {
                                // Validate fields before submitting
                                if (name.isNotBlank() &&
                                    email.isNotBlank() &&
                                    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                                    phone.length >= 10 &&
                                    expertiseList.all { it.isNotBlank() } &&
                                    availabilityList.all { it.isNotBlank() }
                                ) {
                                    isSubmitting = true
                                    scope.launch {
                                        // Simulate a delay before processing
                                        delay(1000) // Adjust delay as needed
                                        val newCounselor = Counselor(
                                            name = name,
                                            expertise = expertiseList.filter { it.isNotBlank() },
                                            availability = availabilityList.filter { it.isNotBlank() },
                                            email = email,
                                            phone = phone
                                        )

                                        viewModel.addCounselor(newCounselor) { success ->
                                            isSubmitting = false
                                            if (success) {
                                                successMessage = "Counselor added successfully"
                                                onAddCounselor()
                                                onDismiss()
                                            } else {
                                                errorMessage = "Failed to add counselor"
                                            }
                                        }
                                    }
                                } else {
                                    errorMessage = "Please fill in all required fields correctly"
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(220, 87, 45), // Custom color for button background
                            contentColor = Color.White // Custom color for button content
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Counselor")
                    }
                }
            }
        }
    }
}

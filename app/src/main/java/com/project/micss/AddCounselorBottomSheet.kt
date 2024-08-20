import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AddCounselorBottomSheet(
    onAddCounselor: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier // Modifier passed down to ensure full-width
) {
    var name by remember { mutableStateOf("") }
    var expertiseList = remember { mutableStateListOf("") }
    var availabilityList = remember { mutableStateListOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val firestore = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Display snackbar message if errorMessage is not null
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            errorMessage = null
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp) // Padding inside the bottom sheet
    ) {
        Text(
            text = "Add Counselor",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Expertise")
        expertiseList.forEachIndexed { index, expertise ->
            OutlinedTextField(
                value = expertise,
                onValueChange = { expertiseList[index] = it },
                label = { Text("Expertise ${index + 1}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
        Button(
            onClick = { expertiseList.add("") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Expertise")
        }

        Text("Availability")
        availabilityList.forEachIndexed { index, availability ->
            OutlinedTextField(
                value = availability,
                onValueChange = { availabilityList[index] = it },
                label = { Text("Availability ${index + 1}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
        Button(
            onClick = { availabilityList.add("") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Availability")
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    if (!isSubmitting) {
                        isSubmitting = true
                        scope.launch {
                            try {
                                val counselor = hashMapOf(
                                    "name" to name,
                                    "expertise" to expertiseList.filter { it.isNotBlank() },
                                    "availability" to availabilityList.filter { it.isNotBlank() },
                                    "email" to email,
                                    "phone" to phone
                                )
                                firestore.collection("counselors").add(counselor).await()
                                onAddCounselor()
                                onDismiss()
                            } catch (e: Exception) {
                                // Set the error message to show the Snackbar
                                errorMessage = "Failed to add counselor: ${e.message}"
                            } finally {
                                isSubmitting = false
                            }
                        }
                    }
                },
                enabled = !isSubmitting
            ) {
                Text("Add")
            }
        }
    }
}

package com.project.micss.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.project.micss.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (date: String, time: String, language: String, age: String) -> Unit
) {
    val context = LocalContext.current
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    // State for error messages
    var dateError by remember { mutableStateOf<String?>(null) }
    var timeError by remember { mutableStateOf<String?>(null) }
    var languageError by remember { mutableStateOf<String?>(null) }
    var ageError by remember { mutableStateOf<String?>(null) }

    // Dropdown Menu state
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("Chichewa", "English")

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                var hasError = false
                // Reset error messages
                dateError = null
                timeError = null
                languageError = null
                ageError = null

                // Validate fields
                if (date.isEmpty()) {
                    dateError = "Specify date"
                    hasError = true
                }
                if (time.isEmpty()) {
                    timeError = "Specify time"
                    hasError = true
                }
                if (language.isEmpty()) {
                    languageError = "Specify language"
                    hasError = true
                }
                if (age.isEmpty()) {
                    ageError = "Specify age"
                    hasError = true
                }

                if (!hasError) {
                    onConfirm(date, time, language, age)
                }
            }) {
                Text("Set", color = Color(220, 87, 45)) // Set primary color
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel", color = Color(220, 87, 45)) // Set primary color
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Set Appointment")
                Text(
                    "MICSS",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(220, 87, 45) // Set primary color
                    )
                )
            }
        },
        text = {
            var textFieldSize by remember { mutableStateOf(IntSize.Zero) }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Date Picker
                TextField(
                    value = date,
                    onValueChange = {},
                    label = { Text("Date") },
                    placeholder = { Text("Click icon to set date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Rounded border
                        .padding(0.dp), // Remove internal padding to fit the border
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            val calendar = Calendar.getInstance()
                            android.app.DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val selectedDate = Calendar.getInstance().apply {
                                        set(year, month, dayOfMonth)
                                    }
                                    date = dateFormatter.format(selectedDate.time)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.set_calendar), // Replace with your date drawable
                                contentDescription = "Select date",
                                tint = Color(220, 87, 45) // Set icon color here
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent, // Remove the line under focus
                        unfocusedIndicatorColor = Color.Transparent // Remove the line under unfocused
                    ),
                    isError = dateError != null,
                    supportingText = { dateError?.let { Text(it, color = Color.Red) } }
                )

                // Time Picker
                TextField(
                    value = time,
                    onValueChange = {},
                    label = { Text("Time") },
                    placeholder = { Text("Click icon to set time") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Rounded border
                        .padding(0.dp), // Remove internal padding to fit the border
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            val calendar = Calendar.getInstance()
                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    val selectedTime = Calendar.getInstance().apply {
                                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                                        set(Calendar.MINUTE, minute)
                                    }
                                    time = timeFormatter.format(selectedTime.time)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.time), // Replace with your time drawable
                                contentDescription = "Select time",
                                tint = Color(220, 87, 45) // Set icon color here
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent, // Remove the line under focus
                        unfocusedIndicatorColor = Color.Transparent // Remove the line under unfocused
                    ),
                    isError = timeError != null,
                    supportingText = { timeError?.let { Text(it, color = Color.Red) } }
                )

                // Language Preference
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size
                        }
                ) {
                    TextField(
                        value = language,
                        onValueChange = {},
                        label = { Text("Language Preference") },
                        placeholder = { Text("Select language") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Rounded border
                            .padding(0.dp), // Remove internal padding to fit the border
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.dropdown_arrow), // Replace with your dropdown arrow drawable
                                    contentDescription = "Select language",
                                    tint = Color(220, 87, 45), // Set icon color here
                                    modifier = Modifier.size(24.dp) // Set the size of the icon
                                )
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent, // Remove the line under focus
                            unfocusedIndicatorColor = Color.Transparent // Remove the line under unfocused
                        ),
                        isError = languageError != null,
                        supportingText = { languageError?.let { Text(it, color = Color.Red) } }
                    )

                    // Ensure DropdownMenu aligns and sizes properly
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                            .background(Color.White)
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .align(Alignment.TopStart) // Align menu to the start of the Box
                    ) {
                        languages.forEach { lang ->
                            DropdownMenuItem(onClick = {
                                language = lang
                                expanded = false
                            }) {
                                Text(
                                    text = lang,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp) // Adjust padding for better appearance
                                )
                            }
                        }
                    }
                }

                // Age
                TextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Rounded border
                        .padding(0.dp), // Remove internal padding to fit the border
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent, // Remove the line under focus
                        unfocusedIndicatorColor = Color.Transparent // Remove the line under unfocused
                    ),
                    isError = ageError != null,
                    supportingText = { ageError?.let { Text(it, color = Color.Red) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewBookAppointmentDialog() {
    BookAppointmentDialog(
        onDismissRequest = {},
        onConfirm = { date, time, language, age ->
            // Handle confirm action
        }
    )
}
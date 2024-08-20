package com.project.micss.ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounselorsContent(
    viewModel: CounselorsContentViewModel = viewModel(),
    onAddCounselorClick: () -> Unit
) {
    val counselors by viewModel.counselors.collectAsState(initial = emptyList())
    var showAddCounselorScreen by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var counselorToDelete by remember { mutableStateOf<Counselor?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Counselors",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.Black
                            )
                            Button(
                                onClick = { showAddCounselorScreen = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(220, 87, 45),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Add")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(counselors) { counselor ->
                    var expanded by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .border(BorderStroke(1.dp, Color.Gray))
                    ) {
                        // Counselor's Name and Actions
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp)
                                .border(BorderStroke(0.dp, Color.Gray))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = counselor.name,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )

                            Box {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More Actions",
                                        tint = Color.Black
                                    )
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .background(Color.White)
                                        .wrapContentWidth()
                                ) {
                                    DropdownMenuItem(onClick = {
                                        // Verify Counselor
                                        viewModel.verifyCounselor(counselor.id)
                                        expanded = false // Close menu
                                    }) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Verify", tint = Color.Green, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Verify", style = MaterialTheme.typography.bodySmall)
                                    }
                                    DropdownMenuItem(onClick = {
                                        // Edit Counselor
                                        // Navigate to edit screen or show dialog
                                        expanded = false // Close menu
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Blue, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Edit", style = MaterialTheme.typography.bodySmall)
                                    }
                                    DropdownMenuItem(onClick = {
                                        // Show delete confirmation dialog
                                        counselorToDelete = counselor
                                        showDeleteConfirmation = true
                                        expanded = false // Close menu
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Delete", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                        // Counselor's Details Table
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(BorderStroke(1.dp, Color.Gray))
                        ) {
                            TableRow("Expertise", counselor.expertise.joinToString("\n") { "• $it" })
                            TableRow("Availability", counselor.availability.joinToString("\n") { "• $it" })
                            TableRow("Email", counselor.email)
                            TableRow("Phone", counselor.phone)
                        }
                    }
                }
            }
        }

        // Use AnimatedVisibility to manage the transition
        AnimatedVisibility(
            visible = showAddCounselorScreen,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            // Full-screen overlay
            AddCounselorScreen(
                onAddCounselor = {
                    showAddCounselorScreen = false
                    // Handle add counselor action here
                },
                onDismiss = {
                    showAddCounselorScreen = false
                    // Handle dismiss action here
                }
            )
        }

        // Delete confirmation dialog
        if (showDeleteConfirmation && counselorToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Confirm Delete") },
                text = { Text("Are you sure you want to delete ${counselorToDelete?.name}? This action cannot be undone.") },
                confirmButton = {
                    TextButton(onClick = {
                        counselorToDelete?.let {
                            viewModel.deleteCounselor(it.id)
                            counselorToDelete = null
                        }
                        showDeleteConfirmation = false
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteConfirmation = false
                        counselorToDelete = null
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun TableRow(title: String, value: String) {
    val shouldAddDivider = value.lines().size < 5 // Adjust based on your needs
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (shouldAddDivider) Modifier.border(BorderStroke(0.5.dp, Color.Gray)) else Modifier)
    ) {
        TableCell(text = title, weight = 1f)
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .then(if (shouldAddDivider) Modifier else Modifier.fillMaxHeight().width(0.dp)),
            color = Color.Gray
        )
        TableCell(text = value, weight = 2f)
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp),
        style = MaterialTheme.typography.bodySmall
    )
}
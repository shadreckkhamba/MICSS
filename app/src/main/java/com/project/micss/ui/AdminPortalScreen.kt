package com.project.micss.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPortalScreen(onLogout: () -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedSection by remember { mutableStateOf("Dashboard") }
    var expanded by remember { mutableStateOf(false) }
    var isLoggingOut by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val isDrawerOpen = drawerState.isOpen

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.padding(top = 56.dp) // Start below the top bar
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                    DrawerItem(
                        text = "Dashboard",
                        isSelected = selectedSection == "Dashboard",
                        icon = Icons.Default.Dashboard
                    ) {
                        selectedSection = "Dashboard"
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem(
                        text = "Clients",
                        isSelected = selectedSection == "Clients",
                        icon = Icons.Default.People
                    ) {
                        selectedSection = "Clients"
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem(
                        text = "Counselors",
                        isSelected = selectedSection == "Counselors",
                        icon = Icons.Default.Person
                    ) {
                        selectedSection = "Counselors"
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem(
                        text = "Resources",
                        isSelected = selectedSection == "Resources",
                        icon = Icons.Default.Book
                    ) {
                        selectedSection = "Resources"
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem(
                        text = "Appointments",
                        isSelected = selectedSection == "Appointments",
                        icon = Icons.Default.Event
                    ) {
                        selectedSection = "Appointments"
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem(
                        text = "Other Settings",
                        isSelected = selectedSection == "Other Settings",
                        icon = Icons.Default.Settings
                    ) {
                        selectedSection = "Other Settings"
                        scope.launch { drawerState.close() }
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Admin Portal") },
                        actions = {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Account",
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .clickable { expanded = true }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Profile") },
                                    onClick = {
                                        expanded = false
                                        // Handle Profile click
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Logout") },
                                    onClick = {
                                        expanded = false
                                        showLogoutDialog = true
                                    }
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(220, 87, 45, 242),
                            titleContentColor = Color.White
                        ),
                        navigationIcon = {
                            Icon(
                                imageVector = if (isDrawerOpen) Icons.Filled.Close else Icons.Default.Menu,
                                contentDescription = "Menu",
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .clickable {
                                        scope.launch {
                                            if (isDrawerOpen) drawerState.close() else drawerState.open()
                                        }
                                    }
                                    .rotate(if (isDrawerOpen) 90f else 0f)
                            )
                        }
                    )
                },
                content = { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 16.dp, // Padding for left
                                end = 16.dp, // Padding for right
                                top = innerPadding.calculateTopPadding(), // Ensure top padding matches the top bar
                                bottom = innerPadding.calculateBottomPadding() // Ensure bottom padding is consistent
                            )
                    ) {
                        if (isLoggingOut) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopCenter),
                                color = Color(220, 87, 45)
                            )
                        }

                        when (selectedSection) {
                            "Dashboard" -> DashboardScreen()
                            "Clients" -> ClientsScreen()
                            "Counselors" -> CounselorsScreen()
                            "Resources" -> ResourcesScreen()
                            "Appointments" -> AppointmentsScreen()
                            "Other Settings" -> OtherSettingsScreen()
                        }
                    }
                }
            )
        }
    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        isLoggingOut = true
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}




@Composable
fun DrawerItem(
    text: String,
    isSelected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val textColor = if (isSelected) Color(220, 87, 45) else Color.Black // Primary color for selected item

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(24.dp) // Adjust size as needed
        )
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun DashboardScreen() {
    Text("Dashboard Content")
}

@Composable
fun ClientsScreen() {
    Text("Clients Content")
}

@Composable
fun CounselorsScreen() {
    Text("Counselors Content")
}

@Composable
fun ResourcesScreen() {
    Text("Resources Content")
}

@Composable
fun AppointmentsScreen() {
    Text("Appointments Content")
}

@Composable
fun OtherSettingsScreen() {
    Text("Other Settings Content")
}
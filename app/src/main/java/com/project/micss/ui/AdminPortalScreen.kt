package com.project.micss.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPortalScreen() {
    var selectedSection by remember { mutableStateOf("Dashboard") }

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
                            .clickable { /* Handle account click */ }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(220, 87, 45, 242),
                    titleContentColor = Color.White
                )
            )
        },
        content = { padding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Sidebar
                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .fillMaxHeight()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    SidebarItem(text = "Dashboard", isSelected = selectedSection == "Dashboard") { selectedSection = "Dashboard" }
                    SidebarItem(text = "Clients", isSelected = selectedSection == "Clients") { selectedSection = "Clients" }
                    SidebarItem(text = "Counselors", isSelected = selectedSection == "Counselors") { selectedSection = "Counselors" }
                    SidebarItem(text = "Resources", isSelected = selectedSection == "Resources") { selectedSection = "Resources" }
                    SidebarItem(text = "Appointments", isSelected = selectedSection == "Appointments") { selectedSection = "Appointments" }
                    SidebarItem(text = "Other Settings", isSelected = selectedSection == "Other Settings") { selectedSection = "Other Settings" }
                }

                // Main content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
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
        }
    )
}

@Composable
fun SidebarItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color.Gray else Color.Transparent
    val textColor = if (isSelected) Color.Blue else Color.Black

    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
            .background(backgroundColor),
        color = textColor
    )
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
package com.project.micss.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.micss.DashboardScreen
import com.project.micss.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AdminPortalScreen(onLogout: () -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    var selectedSection by remember { mutableStateOf("Dashboard") }
    var expanded by remember { mutableStateOf(false) }
    var isLoggingOut by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val isDrawerOpen = drawerState.isOpen

    // State to track refresh trigger
    val dashboardRefreshTrigger = remember { mutableStateOf(false) }
    val isRefreshing by remember { derivedStateOf { dashboardRefreshTrigger.value } }

    // Refresh function
    fun refresh() = scope.launch {
        dashboardRefreshTrigger.value = true
        // Simulate a refresh delay and reset trigger
        delay(1000) // Adjust the delay as needed
        dashboardRefreshTrigger.value = false
    }

    // PullRefresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = ::refresh
    )

    // Instantiate the CounselorsContentViewModel
    val viewModel: CounselorsContentViewModel = viewModel()

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
                        navController.navigate("dashboard")
                    }
                    DrawerItem(
                        text = "Clients",
                        isSelected = selectedSection == "Clients",
                        icon = Icons.Default.People
                    ) {
                        selectedSection = "Clients"
                        scope.launch { drawerState.close() }
                        navController.navigate("clients")
                    }
                    DrawerItem(
                        text = "Counselors",
                        isSelected = selectedSection == "Counselors",
                        icon = Icons.Default.Person
                    ) {
                        selectedSection = "Counselors"
                        scope.launch { drawerState.close() }
                        navController.navigate("counselors")
                    }
                    DrawerItem(
                        text = "Resources",
                        isSelected = selectedSection == "Resources",
                        icon = Icons.Default.Book
                    ) {
                        selectedSection = "Resources"
                        scope.launch { drawerState.close() }
                        navController.navigate("resources")
                    }
                    DrawerItem(
                        text = "Appointments",
                        isSelected = selectedSection == "Appointments",
                        icon = Icons.Default.Event
                    ) {
                        selectedSection = "Appointments"
                        scope.launch { drawerState.close() }
                        navController.navigate("appointments")
                    }
                    DrawerItem(
                        text = "Other Settings",
                        isSelected = selectedSection == "Other Settings",
                        icon = Icons.Default.Settings
                    ) {
                        selectedSection = "Other Settings"
                        scope.launch { drawerState.close() }
                        navController.navigate("other_settings")
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("MICSS Admin") },
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
                                    text = { Text("Refresh") },
                                    onClick = {
                                        expanded = false
                                        refresh() // Trigger the refresh
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
                            .pullRefresh(pullRefreshState) // Apply pull-refresh to the whole content
                            .padding(
                                start = 16.dp, // Padding for left
                                end = 16.dp, // Padding for right
                                top = innerPadding.calculateTopPadding(), // Ensure top padding matches the top bar
                                bottom = innerPadding.calculateBottomPadding() // Ensure bottom padding is consistent
                            )
                    )


                    {
                        NavHost(
                            navController = navController,
                            startDestination = "dashboard",
                            Modifier.fillMaxSize()
                        ) {
                            composable("dashboard") { DashboardScreen(
                                onNavItemClick = { section -> selectedSection = section },
                                refreshTrigger = dashboardRefreshTrigger
                            ) }
                            composable("clients") { ClientsScreen() }
                            composable("counselors") { CounselorsContent(
                                onAddCounselorClick = {
                                    navController.navigate("add_counselor")
                                }
                            ) }
                            composable("search") { SearchScreen() }
                            composable("profile") { ProfileScreen() }
                            composable("settings") { SettingsScreen() }
                            composable("resources") { ResourcesScreen() }
                            composable("appointments") { AppointmentContent() }
                            composable("other_settings") { OtherSettingsScreen() }
                            composable("add_counselor") {
                                AddCounselorScreen(
                                    viewModel = viewModel, // Pass the viewModel here
                                    onAddCounselor = {
                                        // Handle add counselor action here
                                        navController.popBackStack()
                                    },
                                    onDismiss = {
                                        // Handle dismiss action here
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }

                        // PullRefreshIndicator
                        PullRefreshIndicator(
                            refreshing = isRefreshing,
                            state = pullRefreshState,
                            modifier = Modifier.align(Alignment.TopCenter),
                            contentColor = Color(0xFF3F51B5)
                        )
                    }



                },
                bottomBar = {
                    BottomAppBar(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .height(64.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFFCFCECE)) // Slightly off-white background
                            .shadow(8.dp, RoundedCornerShape(24.dp)), // Add the shadow effect
                        containerColor = Color(0xFFCFCECE), // Slightly off-white background color
                        contentColor = Color(0xFF1A1919) // Color for the content (icons)
                    ) {
                        val navItems = listOf("Dashboard", "Search", "Profile", "Settings")
                        navItems.forEach { item ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        painter = painterResource(id = when (item) {
                                            "Dashboard" -> R.drawable.ic_dashboard
                                            "Search" -> R.drawable.ic_search
                                            "Profile" -> R.drawable.ic_profile
                                            "Settings" -> R.drawable.ic_settings
                                            else -> R.drawable.ic_dashboard // Default
                                        }),
                                        contentDescription = item,
                                        modifier = Modifier
                                            .size(38.dp) // Set uniform size for icons
                                            .padding(bottom = 4.dp), // Space between icon and underline
                                        tint = if (selectedSection == item) Color(220, 87, 45) else Color(0xFF1A1919) // Primary color for selected item
                                    )
                                },
                                selected = selectedSection == item,
                                onClick = {
                                    selectedSection = item
                                    // Handle navigation logic here
                                    when (item) {
                                        "Dashboard" -> navController.navigate("dashboard")
                                        "Search" -> navController.navigate("search") // Assuming you have a "search" route
                                        "Profile" -> navController.navigate("profile") // Assuming you have a "profile" route
                                        "Settings" -> navController.navigate("settings") // Assuming you have a "settings" route
                                    }
                                },
                                selectedContentColor = Color.Transparent, // To ensure underline color is shown
                                unselectedContentColor = Color(0xFF1A1919) // Color for unselected items
                            )
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
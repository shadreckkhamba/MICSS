package com.project.micss.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ConnectWithoutContact
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.project.micss.R
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Status bar color
        val window: Window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        window.statusBarColor = Color(0xFFFFFFFF).toArgb()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val selectedSection = remember { mutableStateOf("home") }
    val context = LocalContext.current
    val showLogoutDialog = remember { mutableStateOf(false) }
    val isLoggingOut = remember { mutableStateOf(false) }

    // Sync selectedSection with NavController
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    selectedSection.value = currentRoute ?: "home"

    fun handleLogout(context: Context) {
        // Perform any necessary cleanup here, such as clearing session data or resetting user state

        // Navigate to LoginActivity with flags to clear the activity stack and show the LoginScreen directly
        val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("SHOW_LOGIN_SCREEN", true) // Ensures that the LoginScreen is shown directly
        }
        context.startActivity(intent)

        // Optionally finish the current activity (if this is called from an activity)
        (context as? Activity)?.finish()
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.padding(top = 56.dp) // Start below the top bar
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    DrawerItems(
                        text = "Home",
                        isSelected = selectedSection.value == "home",
                        icon = Icons.Default.Home
                    ) {
                        selectedSection.value = "home"
                        scope.launch { drawerState.close() }
                        navController.navigate("home")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DrawerItems(
                        text = "Counselors",
                        isSelected = selectedSection.value == "counselors",
                        icon = Icons.Default.Person
                    ) {
                        selectedSection.value = "counselors"
                        scope.launch { drawerState.close() }
                        navController.navigate("counselors")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DrawerItems(
                        text = "Appointments",
                        isSelected = selectedSection.value == "appointments",
                        icon = Icons.Default.Event
                    ) {
                        selectedSection.value = "appointments"
                        scope.launch { drawerState.close() }
                        navController.navigate("appointments")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DrawerItems(
                        text = "Chat Sessions",
                        isSelected = selectedSection.value == "chat_sessions",
                        icon = Icons.Default.Chat
                    ) {
                        selectedSection.value = "chat_sessions"
                        scope.launch { drawerState.close() }
                        navController.navigate("chat_sessions")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DrawerItems(
                        text = "Music Therapy",
                        isSelected = selectedSection.value == "music_therapy",
                        icon = Icons.Default.MusicNote
                    ) {
                        selectedSection.value = "music_therapy"
                        scope.launch { drawerState.close() }
                        navController.navigate("music_therapy")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DrawerItems(
                        text = "Meditation",
                        isSelected = selectedSection.value == "meditation",
                        icon = Icons.Default.SelfImprovement
                    ) {
                        selectedSection.value = "meditation"
                        scope.launch { drawerState.close() }
                        navController.navigate("meditation")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DrawerItems(
                        text = "Groups",
                        isSelected = selectedSection.value == "groups",
                        icon = Icons.Default.Groups
                    ) {
                        selectedSection.value = "groups"
                        scope.launch { drawerState.close() }
                        navController.navigate("groups")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DrawerItems(
                        text = "Connect",
                        isSelected = selectedSection.value == "connect",
                        icon = Icons.Default.ConnectWithoutContact
                    ) {
                        selectedSection.value = "connect"
                        scope.launch { drawerState.close() }
                        navController.navigate("connect")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    DrawerItems(
                        text = "Logout",
                        isSelected = false,
                        icon = Icons.Default.ExitToApp
                    ) {
                        showLogoutDialog.value = true
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    if (currentRoute != "counselors" && currentRoute != "appointments") {
                        TopAppBar(
                            onMenuClick = {
                                scope.launch { drawerState.open() }
                            }
                        )
                    }
                },
                bottomBar = {
                    if (currentRoute != "counselors" && currentRoute != "appointments") {
                        BottomNavigationBar(navController)
                    }
                },
                floatingActionButton = {
                    if (currentRoute != "counselors" && currentRoute != "appointments") {
                        AIFloatingActionButton()
                    }
                },
                floatingActionButtonPosition = FabPosition.End,
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                content = { padding ->
                    Box(
                        modifier = Modifier.padding(padding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable("home") {
                                AnimatedVisibility(
                                    visible = selectedSection.value == "home",
                                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                        initialOffsetX = { it },
                                        animationSpec = tween(500)
                                    ),
                                    exit = fadeOut(animationSpec = tween(500)) + slideOutHorizontally(
                                        targetOffsetX = { -it },
                                        animationSpec = tween(500)
                                    )
                                ) {
                                    HomeScreen()
                                }
                            }
                            composable("counselors") {
                                AnimatedVisibility(
                                    visible = selectedSection.value == "counselors",
                                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                        initialOffsetX = { it },
                                        animationSpec = tween(500)
                                    ),
                                    exit = fadeOut(animationSpec = tween(500)) + slideOutHorizontally(
                                        targetOffsetX = { -it },
                                        animationSpec = tween(500)
                                    )
                                ) {
                                    CounselorsScreen(navController = navController)
                                }
                            }
                            composable("appointments") {
                                AnimatedVisibility(
                                    visible = selectedSection.value == "appointments",
                                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                        initialOffsetX = { it },
                                        animationSpec = tween(500)
                                    ),
                                    exit = fadeOut(animationSpec = tween(500)) + slideOutHorizontally(
                                        targetOffsetX = { -it },
                                        animationSpec = tween(500)
                                    )
                                ) {
                                    AppointmentsScreen(navController = navController)
                                }
                            }
                            composable("chat_sessions") {
                                AnimatedVisibility(
                                    visible = selectedSection.value == "chat_sessions",
                                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                        initialOffsetX = { it },
                                        animationSpec = tween(500)
                                    ),
                                    exit = fadeOut(animationSpec = tween(500)) + slideOutHorizontally(
                                        targetOffsetX = { -it },
                                        animationSpec = tween(500)
                                    )
                                ) {
                                    PlaceholderScreen("Chat Sessions")
                                }
                            }
                            composable("music_therapy") {
                                AnimatedVisibility(
                                    visible = selectedSection.value == "music_therapy",
                                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                        initialOffsetX = { it },
                                        animationSpec = tween(500)
                                    ),
                                    exit = fadeOut(animationSpec = tween(500)) + slideOutHorizontally(
                                        targetOffsetX = { -it },
                                        animationSpec = tween(500)
                                    )
                                ) {
                                    PlaceholderScreen("Music Therapy")
                                }
                            }
                            composable("meditation") {
                                AnimatedVisibility(
                                    visible = selectedSection.value == "meditation",
                                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                        initialOffsetX = { it },
                                        animationSpec = tween(500)
                                    ),
                                    exit = fadeOut(animationSpec = tween(500)) + slideOutHorizontally(
                                        targetOffsetX = { -it },
                                        animationSpec = tween(500)
                                    )
                                ) {
                                    PlaceholderScreen("Meditation")
                                }
                            }
                            composable("groups") {
                                AnimatedVisibility(
                                    visible = selectedSection.value == "groups",
                                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                        initialOffsetX = { it },
                                        animationSpec = tween(500)
                                    ),
                                    exit = fadeOut(animationSpec = tween(500)) + slideOutHorizontally(
                                        targetOffsetX = { -it },
                                        animationSpec = tween(500)
                                    )
                                ) {
                                    PlaceholderScreen("Groups")
                                }
                            }
                            composable("connect") {
                                AnimatedVisibility(
                                    visible = selectedSection.value == "connect",
                                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                        initialOffsetX = { it },
                                        animationSpec = tween(500)
                                    ),
                                    exit = fadeOut(animationSpec = tween(500)) + slideOutHorizontally(
                                        targetOffsetX = { -it },
                                        animationSpec = tween(500)
                                    )
                                ) {
                                    PlaceholderScreen("Connect")
                                }
                            }
                        }
                    }
                }
            )

            // Logout confirmation dialog
            if (showLogoutDialog.value) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog.value = false },
                    title = { Text("Logout Confirmation") },
                    text = { Text("Are you sure you want to logout?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isLoggingOut.value = true
                                showLogoutDialog.value = false
                                handleLogout(context)
                            }
                        ) {
                            Text("Logout")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showLogoutDialog.value = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    )
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = Color(0xFFDC572D)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.find_counselors_icon),
                    contentDescription = "Find Counselors",
                    tint = Color.White
                )
            },
            label = {
                Text("Counselors", color = Color.White)
            },
            selected = currentRoute == "counselors",
            onClick = {
                if (currentRoute != "counselors") {
                    navController.navigate("counselors") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.resources_icon),
                    contentDescription = "Resources",
                    tint = Color.White
                )
            },
            label = {
                Text("Resources", color = Color.White)
            },
            selected = currentRoute == "resources",
            onClick = {
                if (currentRoute != "resources") {
                    navController.navigate("resources") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.appointments_icon),
                    contentDescription = "Appointments",
                    tint = Color.White
                )
            },
            label = {
                Text("Appointments", color = Color.White)
            },
            selected = currentRoute == "appointments",
            onClick = {
                if (currentRoute != "appointments") {
                    navController.navigate("appointments") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.profile_icon),
                    contentDescription = "Profile",
                    tint = Color.White
                )
            },
            label = {
                Text("Profile", color = Color.White)
            },
            selected = currentRoute == "profile",
            onClick = {
                if (currentRoute != "profile") {
                    navController.navigate("profile") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "MICSS",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = { /* Handle notifications click */ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.notification_icon),
                    contentDescription = "Notifications",
                    tint = Color.Black
                )
            }
            IconButton(onClick = { /* Handle settings click */ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.settings_icon),
                    contentDescription = "Settings",
                    tint = Color.Black
                )
            }
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.menu_icon),
                    contentDescription = "Menu",
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFFFFF))
    )
}

@Composable
fun DrawerItems(
    text: String,
    isSelected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (isSelected) Color(220, 87, 45) else Color(0xFF1A1919),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = if (isSelected) Color(220, 87, 45) else Color(0xFF1A1919),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text(text = "Ask MICSS AI or Search") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(24.dp))
            .background(Color.White, shape = RoundedCornerShape(24.dp))
            .height(56.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}

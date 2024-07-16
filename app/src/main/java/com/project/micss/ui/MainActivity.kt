package com.project.micss.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.project.micss.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Set status bar color
            val window: Window = this.window
            window.statusBarColor = Color(0xFFDC572D).toArgb()
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
            MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            if (currentBackStackEntry?.destination?.route != "counselors") {
                TopAppBar()
            }
        },
        bottomBar = {
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            if (currentBackStackEntry?.destination?.route != "counselors") {
                BottomNavigationBar(navController)
            }
        },
        floatingActionButton = {
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            if (currentBackStackEntry?.destination?.route != "counselors") {
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
                    composable("home") { HomeScreen(snackbarHostState) }
                    composable("counselors") {
                        // Show the CounselorsScreen without Scaffold components
                        CounselorsScreen()
                    }
                    // Add other composable destinations here
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(snackbarHostState: SnackbarHostState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SearchBar()
        Text(
            text = "Trending Topics",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp, start = 16.dp)
                .align(Alignment.Start)
        )
        TrendingTopicsSection()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "MICSS",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = { /* Handle notifications click */ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.notification_icon),
                    contentDescription = "Notifications",
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* Handle settings click */ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.settings_icon),
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* Handle offscreen menu click */ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.menu_icon),
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDC572D))
    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingTopicsSection() {
    val topics = listOf(
        Topic(id = "1", title = "Mental Health", imageUrl = "https://via.placeholder.com/150"),
        Topic(id = "2", title = "Anxiety", imageUrl = "https://via.placeholder.com/150"),
        Topic(id = "3", title = "Depression", imageUrl = "https://via.placeholder.com/150"),
        Topic(id = "4", title = "Stress Management", imageUrl = "https://via.placeholder.com/150"),
        Topic(id = "5", title = "Sleep Hygiene", imageUrl = "https://via.placeholder.com/150"),
        Topic(id = "6", title = "Physical Health", imageUrl = "https://via.placeholder.com/150")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(topics) { topic ->
            TopicCard(topic)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicCard(topic: Topic) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(4.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable(
                onClick = { /* Handle click */ },
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true, color = Color.Gray)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = topic.imageUrl),
                contentDescription = topic.title,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = topic.title,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavHostController) {
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
                Text(
                    "Counselors",
                    color = Color.White
                )
            },
            selected = currentRoute == "counselors",
            onClick = { navController.navigate("counselors") }
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
                Text(
                    "Resources",
                    color = Color.White
                )
            },
            selected = currentRoute == "resources",
            onClick = { navController.navigate("resources") }
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
                Text(
                    "Appointment",
                    color = Color.White
                )
            },
            selected = currentRoute == "appointments",
            onClick = { navController.navigate("appointments") }
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
                Text(
                    "Profile",
                    color = Color.White
                )
            },
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") }
        )
    }
}


@Composable
fun AIFloatingActionButton() {
    FloatingActionButton(
        onClick = { /* Handle click */ },
        containerColor = Color(0xFFDC572D), // Use containerColor instead of backgroundColor
        contentColor = Color.White,
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ai_icon),
            contentDescription = "AI",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}
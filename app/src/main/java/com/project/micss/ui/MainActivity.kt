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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // Set status bar color
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
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()


    Scaffold(
        topBar = {
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            if (currentBackStackEntry?.destination?.route != "counselors" &&
                currentBackStackEntry?.destination?.route != "appointments") {
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
                    composable("home") { HomeScreen() }
                    composable("counselors") {
                        // Show the CounselorsScreen without Scaffold components
                        CounselorsScreen(navController = navController)
                    }
                    composable("appointments") {
                        // Show the AppointmentsScreen with its own custom top bar
                        AppointmentsScreen(navController = navController)
                    }
                    // Add other composable destinations here
                }
            }
        }
    )
}

@Composable
fun HomeScreen() {
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
            IconButton(onClick = { /* Handle offscreen menu click */ }) {
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
                indication = rememberRipple(bounded = true, color = Color(220, 87, 4))
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
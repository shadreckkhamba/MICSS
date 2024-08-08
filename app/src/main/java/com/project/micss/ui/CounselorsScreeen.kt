package com.project.micss.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetLayout
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.project.micss.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CounselorsScreen(navController: NavController) {
    val viewModel: AppointmentViewModel = viewModel()
    var isLoading by remember { mutableStateOf(true) }
    var selectedCounselor by remember { mutableStateOf<Counselor?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var submitInProgress by remember { mutableStateOf(false) }
    val counselors by viewModel<CounselorsViewModel>().counselors.collectAsState()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            delay(2000)
            isLoading = false
        } catch (e: Exception) {
            // Handle error
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = {
                selectedCounselor?.let { counselor ->
                    Surface(
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(LocalConfiguration.current.screenHeightDp.dp * 0.9f) // Cover 90% of the screen height
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Profile Picture
                                Image(
                                    painter = rememberAsyncImagePainter(model = counselor.profilePictureUrl),
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                // Counselor Name
                                Text(
                                    text = counselor.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f) // Take only the necessary space
                                )

                                IconButton(onClick = {
                                    scope.launch { sheetState.hide() }
                                    selectedCounselor = null
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Expertise Card
                            Text("Expertise", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    counselor.expertise.forEach { expertise ->
                                        Text(" $expertise", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Availability Card
                            Text("Availability", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                elevation = CardDefaults.cardElevation(2.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    counselor.availability.forEach { availability ->
                                        Text(" $availability", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Contact Details Card
                            Column {
                                Text("Contact Details", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    elevation = CardDefaults.cardElevation(2.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("Email: ${counselor.email}", style = MaterialTheme.typography.bodyMedium)
                                        Text("Phone: ${counselor.phone}", style = MaterialTheme.typography.bodyMedium)
                                        Text("District: ${counselor.district}", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    // WhatsApp Button
                                    WhatsAppButton(counselorPhoneNumber = counselor.phone)

                                    // Phone Call Button
                                    CallButton(counselorPhoneNumber = counselor.phone)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Spacer(modifier = Modifier.weight(1f)) // Push the button to the bottom

                            Button(
                                onClick = { showDialog = true }, // Show dialog on click
                                colors = ButtonDefaults.buttonColors(containerColor = Color(220, 87, 45)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text("Book Appointment with ${counselor.name}", color = Color.White)
                            }
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { navController.navigate("home") }) {
                                    Icon(imageVector = Icons.Rounded.Home, contentDescription = "Home", tint = Color.Black)
                                }
                                Text("MICSS", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
                            }
                        },
                        actions = {
                            IconButton(onClick = { /* Handle search action */ }) {
                                Icon(Icons.Rounded.Search, contentDescription = "Search", tint = Color.Black)
                            }
                            IconButton(onClick = { /* Handle filter action */ }) {
                                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter), contentDescription = "Filter", tint = Color.Black)
                            }
                        },
                        modifier = Modifier.height(64.dp),
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFFFFF))
                    )
                },
                content = {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 72.dp, start = 8.dp, end = 8.dp)) {
                        Text(
                            text = "Counselors",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        AnimatedVisibility(
                            visible = isLoading,
                            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }

                        AnimatedVisibility(
                            visible = !isLoading && counselors.isEmpty(),
                            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "No counselors to show, check your internet connection",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        AnimatedVisibility(
                            visible = !isLoading && counselors.isNotEmpty(),
                            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(counselors) { counselor ->
                                    DisplayCounselorInfo(
                                        counselor = counselor,
                                        onClick = {
                                            selectedCounselor = it
                                            scope.launch {
                                                sheetState.show()
                                            }
                                        },
                                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                },
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }
            )
        }

// logic to show BookAppointmentDialog when showDialog is true
        if (showDialog) {
            BookAppointmentDialog(
                onDismissRequest = { showDialog = false },
                onConfirm = { date, time, language, age ->
                    val message = "Appointment scheduled with ${selectedCounselor?.name} on $date at $time"

                    // Prevent multiple submissions appointment
                    if (!submitInProgress) {
                        submitInProgress = true

                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                        // Navigating to Appointments Screen with appointment details
                        navController.navigate("appointments") {
                            popUpTo("counselors") { inclusive = true }
                        }

                        showDialog = false
                        submitInProgress = false
                    }
                },
                viewModel = viewModel(),
                counselorName = selectedCounselor?.name ?: "Unknown",
                expertise = listOf(selectedCounselor?.expertise?.firstOrNull() ?: "Unknown"),
                profilePictureUrl = selectedCounselor?.profilePictureUrl ?: ""
            )
        }


        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { data ->
                Snackbar(
                    action = {
                        Icon(Icons.Default.Check, contentDescription = "Check", modifier = Modifier.size(20.dp))
                    },
                    content = {
                        Text(text = data.visuals.message)
                    }
                )
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


//displaying the counselor's name, picture, and expertise
@Composable
fun DisplayCounselorInfo(
    counselor: Counselor,
    onClick: (Counselor) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val ripple = rememberRipple(color = Color(0xFF03A9F4))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp))
            .background(Color.White, RoundedCornerShape(24.dp))
            .clickable(
                onClick = { onClick(counselor) },
                indication = ripple,
                interactionSource = interactionSource
            )
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(counselor.profilePictureUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = counselor.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = counselor.expertise[0],
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

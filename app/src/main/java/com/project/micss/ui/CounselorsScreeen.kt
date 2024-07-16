package com.project.micss.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.project.micss.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounselorsScreen(viewModel: CounselorsViewModel = viewModel()) {
    // Collecting state from ViewModel
    val counselors by viewModel.counselors.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Counselors", color = Color.White) },
                modifier = Modifier.height(48.dp), // Adjusted height to match your requirements
                colors = topAppBarColors(
                    containerColor = Color(220, 87, 45)
                )
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp, start = 8.dp, end = 8.dp) // Adjusted top padding to accommodate TopAppBar height
            ) {
                counselors.forEach { counselor ->
                    DisplayCounselorInfo(counselor = counselor) {
                        onCounselorClick(counselor)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    )
}

fun onCounselorClick(counselor: Counselor) {
    // Implement navigation to detailed view or show detailed information
    // You can use a navigation framework or compose's NavController
}

@Composable
fun DisplayCounselorInfo(counselor: Counselor, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val ripple = rememberRipple(color = Color(0xFF03A9F4)) // Using RGB combo directly

    Box(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp) // Reduced vertical padding
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp)) // Increased rounded corners
            .background(Color.White, RoundedCornerShape(24.dp))
            .clickable(
                onClick = onClick,
                indication = ripple, // Custom ripple color
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
                        .data(data = counselor.profilePictureUrl)
                        .apply {
                            placeholder(R.drawable.placeholder_image)
                            error(R.drawable.error_image)
                            fallback(R.drawable.placeholder_image)
                        }
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp) // Adjust image size to fit better within the card
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp)) // Reduced spacer width
            Column {
                Text(
                    text = counselor.name,
                    style = MaterialTheme.typography.headlineSmall, // Increased text size
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp)) // Reduced height spacer
                Text(
                    text = counselor.expertise,
                    style = MaterialTheme.typography.titleMedium, // Increased text size
                    color = Color.Gray
                )
            }
        }
    }
}

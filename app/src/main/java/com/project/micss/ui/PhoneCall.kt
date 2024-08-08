package com.project.micss.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


//navigating to phone call
@Composable
fun CallButton(counselorPhoneNumber: String) {
    val context = LocalContext.current

    Button(
        onClick = { makePhoneCall(context, counselorPhoneNumber) },
        colors = ButtonDefaults.buttonColors(containerColor = Color(37, 99, 235)),
        modifier = Modifier.width(150.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Phone, // Change this to the appropriate icon if needed
            contentDescription = "Phone Call",
            tint = Color.White,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text("Phone Call", color = Color.White)
    }
}

fun makePhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}

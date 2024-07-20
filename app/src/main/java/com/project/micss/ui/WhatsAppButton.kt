package com.project.micss.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.project.micss.R

@Composable
fun WhatsAppButton(counselorPhoneNumber: String) {
    val context = LocalContext.current

    Button(
        onClick = { openWhatsApp(context, counselorPhoneNumber) },
        colors = ButtonDefaults.buttonColors(containerColor = Color(37, 211, 102)),
        modifier = Modifier.width(150.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.icon_whatsapp), // WhatsApp icon resource ID
            contentDescription = "WhatsApp",
            tint = Color.White,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text("WhatsApp", color = Color.White)
    }
}

fun openWhatsApp(context: Context, phoneNumber: String) {
    try {
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.whatsapp")
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        // Optionally handle the error, e.g., show a Toast message
    }
}

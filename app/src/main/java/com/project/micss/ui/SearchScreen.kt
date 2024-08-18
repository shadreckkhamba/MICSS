package com.project.micss.ui


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen() {
    Text(
        text = "Search Screen. To be Implemented later",
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        textAlign = TextAlign.Center
    )
}
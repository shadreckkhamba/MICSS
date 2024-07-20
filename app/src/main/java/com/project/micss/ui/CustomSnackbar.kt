package com.project.micss.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier
            .padding(horizontal = 16.dp) // Add horizontal padding
            .fillMaxWidth(), // Ensure it fills the width with padding
        action = {
            snackbarData.visuals.actionLabel?.let { label ->
                TextButton(onClick = { snackbarData.performAction() }) {
                    Text(label)
                }
            }
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle, // Use the tick icon
                contentDescription = "Success",
                tint = Color.Green,
                modifier = Modifier.size(24.dp) // Increase icon size
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(snackbarData.visuals.message)
        }
    }
}

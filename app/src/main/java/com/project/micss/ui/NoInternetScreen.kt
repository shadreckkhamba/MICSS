import android.content.Context
import android.os.Process
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

@Composable
fun NoInternetScreen() {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(245, 245, 245, 242)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Fixed this line
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Failed to load MICSS Check your internet connection",
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color(
                                0,
                                0,
                                0,
                                255
                            )
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = { exitApp(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(220, 87, 45)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Okay", color = Color(241, 240, 240, 242))
                        }
                    }
                }
            }
        }
    )
}

private fun exitApp(context: Context) {
    // Terminate the app
    Process.killProcess(Process.myPid())
}

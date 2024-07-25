import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.micss.R
import com.project.micss.ui.Counselor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounselorDetailBottomSheet(
    counselor: Counselor,
    onScheduleAppointmentClick: () -> Unit,
    onDismiss: () -> Unit
) {
    BottomSheetScaffold(
        sheetContent = {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = counselor.name,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.placeholder_image), // Placeholder image or counselor's actual profile picture
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Expertise: ${counselor.expertise}",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Availability: ${counselor.availability}",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Email: ${counselor.email}",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = onScheduleAppointmentClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Schedule com.project.micss.ui.Appointment")
                }
            }
        },
        sheetContentColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetPeekHeight = 0.dp, // Fully expanded
        modifier = Modifier.fillMaxWidth(),
        content = { }
    )
}
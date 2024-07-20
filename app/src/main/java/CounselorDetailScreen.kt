//package com.project.micss.ui
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.project.micss.R
//
//@Composable
//fun CounselorDetailScreen(counselor: Counselor) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Display Counselor's Profile Picture
//        Image(
//            painter = painterResource(id = R.drawable.placeholder_image), // Replace with actual image resource or URL
//            contentDescription = "Counselor Profile Picture",
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(shape = RoundedCornerShape(8.dp))
//                .padding(top = 8.dp),
//            contentScale = ContentScale.Crop
//        )
//
//        // Display Counselor's Name
//        Text(
//            text = counselor.name,
//            style = MaterialTheme.typography.h5,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//
//        // Display Counselor's Expertise
//        Text(
//            text = "Expertise: ${counselor.expertise}",
//            style = MaterialTheme.typography.body1,
//            modifier = Modifier.padding(vertical = 4.dp)
//        )
//
//        // Display Counselor's Availability
//        Text(
//            text = "Availability: ${counselor.availability}",
//            style = MaterialTheme.typography.body1,
//            modifier = Modifier.padding(vertical = 4.dp)
//        )
//
//        // Display Counselor's Contact Details
//        Text(
//            text = "Contact Details: ${counselor.contactDetails}",
//            style = MaterialTheme.typography.body1,
//            modifier = Modifier.padding(vertical = 4.dp)
//        )
//
//        // Display Counselor's Email
//        Text(
//            text = "Email: ${counselor.email}",
//            style = MaterialTheme.typography.body1,
//            modifier = Modifier.padding(vertical = 4.dp)
//        )
//
//        // Add more details as needed (e.g., office hours, additional qualifications, etc.)
//    }
//}
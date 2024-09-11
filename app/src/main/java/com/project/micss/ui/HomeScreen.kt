package com.project.micss.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.project.micss.R

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

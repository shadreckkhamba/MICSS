package com.project.micss


import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.project.micss.ui.DashboardViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    onBellClick: () -> Unit = {},
    onNavItemClick: (String) -> Unit = {},
    refreshTrigger: MutableState<Boolean> // Add this parameter
) {
    val dashboardState by viewModel.dashboardState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val refreshScope = rememberCoroutineScope()

    // Refresh function
    fun refresh() = refreshScope.launch {
        viewModel.refreshDashboardData()
    }

    // Observe the refresh trigger from the dropdown menu
    LaunchedEffect(refreshTrigger.value) {
        if (refreshTrigger.value) {
            refresh()
            refreshTrigger.value = false // Reset the trigger after refreshing
        }
    }

    // PullRefresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = ::refresh
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title Row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dashboard",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bell), // Replace with your bell icon resource
                        contentDescription = "Notifications",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onBellClick() }, // Make the icon clickable
                        tint = Color(0xFF3F51B5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Space between title and cards

            // Cards displaying various dashboard data
            if (!isRefreshing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DashboardCard(
                        iconRes = R.drawable.ic_counselors,
                        label = "Counselors",
                        count = dashboardState.counselorsCount,
                        iconColor = Color(0xFF3F51B5),
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        iconRes = R.drawable.ic_clients,
                        label = "Clients",
                        count = dashboardState.clientsCount,
                        iconColor = Color(0xFFF44336),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DashboardCard(
                        iconRes = R.drawable.ic_appointments_scheduled,
                        label = "Scheduled Appointments",
                        count = dashboardState.scheduledAppointmentsCount,
                        iconColor = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        iconRes = R.drawable.ic_appointment_canceled,
                        label = "Canceled Appointments",
                        count = dashboardState.canceledAppointmentsCount,
                        iconColor = Color(0xFF00155F),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DashboardCard(
                        iconRes = R.drawable.ic_appointment_completed,
                        label = "Completed Appointments",
                        count = dashboardState.completedAppointmentsCount,
                        iconColor = Color(0xFF9C27B0),
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        iconRes = R.drawable.ic_resources,
                        label = "Resources",
                        count = dashboardState.resourcesCount,
                        iconColor = Color(0xFF00BCD4),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DashboardCard(
                        iconRes = R.drawable.ic_stakeholders,
                        label = "Stakeholders",
                        count = dashboardState.stakeholdersCount,
                        iconColor = Color(0xFF0C58B4),
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        iconRes = R.drawable.ic_users,
                        label = "All Users",
                        count = dashboardState.usersCount,
                        iconColor = Color(0xFFA23004),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // PullRefreshIndicator
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Color(0xFFA23004)
        )
    }
}



@Composable
fun DashboardCard(iconRes: Int, label: String, count: Int, iconColor: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // Elevation for shadow effect
        modifier = modifier
            .height(130.dp) // Slightly increased height for better content fitting
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp) // Adjusted padding to fit content
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp), // Reduced icon size
                    tint = iconColor
                )
                Spacer(modifier = Modifier.width(8.dp)) // Add spacing between icon and text
                Text(
                    text = count.toString(),
                    fontSize = 20.sp, // Slightly reduced size for better fit
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 14.sp, // Reduced font size for the label
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                maxLines = 2, // Allow the label to wrap to a second line if necessary
                overflow = TextOverflow.Ellipsis // Ellipsis if the text is too long
            )
        }
    }
}
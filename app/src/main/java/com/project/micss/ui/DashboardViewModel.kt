package com.project.micss.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val repository = DashboardRepository()

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        refreshDashboardData()
    }

    // Function to fetch and update the dashboard data
    fun refreshDashboardData() {
        viewModelScope.launch {
            _isRefreshing.value = true

            try {
                val counselorsCount = repository.getCounselorsCount().first()
                val clientsCount = repository.getClientsCount().first()
                val scheduledAppointmentsCount = repository.getScheduledAppointmentsCount().first()
                val canceledAppointmentsCount = repository.getCanceledAppointmentsCount().first()
                val completedAppointmentsCount = repository.getCompletedAppointmentsCount().first()
                val resourcesCount = repository.getResourcesCount().first()
                val stakeholdersCount = repository.getStakeholdersCount().first()
                val totalUserCount = repository.getTotalUsersCount().first()

                _dashboardState.value = DashboardState(
                    counselorsCount = counselorsCount,
                    clientsCount = clientsCount,
                    scheduledAppointmentsCount = scheduledAppointmentsCount,
                    canceledAppointmentsCount = canceledAppointmentsCount,
                    completedAppointmentsCount = completedAppointmentsCount,
                    resourcesCount = resourcesCount,
                    stakeholdersCount = stakeholdersCount,
                    usersCount = totalUserCount
                )
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
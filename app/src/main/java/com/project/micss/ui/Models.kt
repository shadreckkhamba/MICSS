package com.project.micss.ui

// Data class representing a Topic
data class Topic(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = ""
)

data class Appointment(
    val id: String = "",
    val counselorName: String = "",
    val date: String = "",
    val time: String = "",
    val clientName: String = "",
    val status: String = "Pending",
    val expertise: List<String> = listOf(),
    val profilePictureUrl: String = "",
    val age: String = "",
    val language: String = "",
    val userId: String = ""
)
data class DashboardState(
    val counselorsCount: Int = 0,
    val clientsCount: Int = 0,
    val scheduledAppointmentsCount: Int = 0,
    val canceledAppointmentsCount: Int = 0,
    val completedAppointmentsCount: Int = 0,
    val resourcesCount: Int = 0,
    val stakeholdersCount: Int = 0,
    val usersCount: Int = 0
)

data class Counselor(
    val id: String = "",
    val name: String = "",
    val expertise: List<String> = emptyList(),
    val profilePictureUrl: String = "",
    val availability: List<String> = emptyList(),
    val contactDetails: String = "",
    val phone: String = "",
    val district: String = "",
    val email: String = "",
    val verified: Boolean = false // Renamed field to match Firestore expectations
)
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

//package com.project.micss.ui
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//
//class AppointmentViewModelFactory(private val userId: String) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(AppointmentViewModel::class.java)) {
//            return AppointmentViewModel(userId) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
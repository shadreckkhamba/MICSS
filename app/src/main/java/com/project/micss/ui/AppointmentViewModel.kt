package com.project.micss.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val appointmentsCollection = firestore.collection("appointments")

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> get() = _appointments

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading


    init {
        fetchAppointments()
    }

    private fun fetchAppointments() {
        viewModelScope.launch {
            _isLoading.value = true // Set loading to true
            // Simulate a loading delay
            delay(2000L)

            appointmentsCollection.addSnapshotListener { snapshot, e ->
                _isLoading.value = false // Set loading to false
                if (e != null) {
                    Log.e("AppointmentViewModel", "Error fetching appointments", e)
                    return@addSnapshotListener
                }

                val fetchedAppointments = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Appointment::class.java)?.copy(id = document.id)
                } ?: emptyList()

                _appointments.value = fetchedAppointments
            }
        }
    }


    fun addAppointment(appointment: Appointment) {
        viewModelScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // Check for existing appointment
                appointmentsCollection
                    .whereEqualTo("counselorName", appointment.counselorName)
                    .whereEqualTo("date", appointment.date)
                    .whereEqualTo("time", appointment.time)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            //When no existing appointment is found, proceed to add one
                            val documentRef = appointmentsCollection.document()
                            val newAppointment = appointment.copy(id = documentRef.id, status = "Scheduled")
                            documentRef.set(newAppointment)
                                .addOnSuccessListener {
                                    //i'm including logs for debugging
                                    Log.d("AppointmentViewModel", "Appointment added successfully")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("AppointmentViewModel", "Error adding appointment", exception)
                                }
                        } else {
                            Log.d("AppointmentViewModel", "Appointment already exists")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("AppointmentViewModel", "Error checking existing appointment", exception)
                    }
            } else {
                Log.e("AppointmentViewModel", "User not authenticated")
            }
        }
    }

    //function for updating an appointment
    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            appointmentsCollection.document(appointment.id)
                .set(appointment)
                .addOnSuccessListener {
                    Log.d("AppointmentViewModel", "Appointment updated successfully")
                }
                .addOnFailureListener { exception ->
                    Log.e("AppointmentViewModel", "Error updating appointment", exception)
                }
        }
    }

    //function for deleting an existing appointment
    fun deleteAppointment(appointmentId: String) {
        viewModelScope.launch {
            appointmentsCollection.document(appointmentId)
                .delete()
                .addOnSuccessListener {
                    Log.d("AppointmentViewModel", "Appointment deleted successfully")
                }
                .addOnFailureListener { exception ->
                    Log.e("AppointmentViewModel", "Error deleting appointment", exception)
                }
        }
    }
}

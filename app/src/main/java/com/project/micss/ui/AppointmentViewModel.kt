package com.project.micss.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val appointmentsCollection = firestore.collection("appointments")

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> get() = _appointments

    init {
        fetchAppointments()
    }

    private fun fetchAppointments() {
        viewModelScope.launch {
            appointmentsCollection.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val fetchedAppointments = snapshot?.documents?.map { document ->
                    document.toObject(Appointment::class.java)?.copy(id = document.id)
                } ?: emptyList()

                _appointments.value = fetchedAppointments as List<Appointment>
            }
        }
    }

    fun addAppointment(appointment: Appointment) {
        viewModelScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val documentRef = appointmentsCollection.document()
                val newAppointment = appointment.copy(id = documentRef.id)
                documentRef.set(newAppointment)
                    .addOnSuccessListener {
                        Log.d("AppointmentViewModel", "Appointment added successfully")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("AppointmentViewModel", "Error adding appointment", exception)
                    }
            } else {
                Log.e("AppointmentViewModel", "User not authenticated")
            }
        }
    }



    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            appointmentsCollection.document(appointment.id)
                .set(appointment)
                .addOnSuccessListener {
                    // Handle success
                }
                .addOnFailureListener { exception ->
                    // Handle error
                }
        }
    }

    fun deleteAppointment(appointmentId: String) {
        viewModelScope.launch {
            appointmentsCollection.document(appointmentId)
                .delete()
                .addOnSuccessListener {
                    // Handle success
                }
                .addOnFailureListener { exception ->
                    // Handle error
                }
        }
    }
}

package com.project.micss.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class CounselorsContentViewModel : ViewModel() {
    private val _counselors = MutableStateFlow<List<Counselor>>(emptyList())
    val counselors: StateFlow<List<Counselor>> = _counselors

    init {
        fetchCounselors()
    }

    private fun fetchCounselors() {
        val db = FirebaseFirestore.getInstance()
        db.collection("counselors")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener

                val counselorsList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Counselor::class.java)?.copy(id = doc.id)
                }

                _counselors.value = counselorsList
            }
    }

    fun addCounselor(counselor: Counselor, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("counselors")
            .add(counselor)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun verifyCounselor(counselorId: String) {
        viewModelScope.launch {
            try {
                val counselorRef = FirebaseFirestore.getInstance().collection("counselors").document(counselorId)
                counselorRef.update("verified", true).await()
                // Optionally, update the local state to reflect the change
            } catch (e: Exception) {
                // Handle error
                Log.e("CounselorsContentViewModel", "Error verifying counselor", e)
            }
        }
    }

    fun deleteCounselor(counselorId: String) {
        viewModelScope.launch {
            try {
                val counselorRef = FirebaseFirestore.getInstance().collection("counselors").document(counselorId)
                counselorRef.delete().await()
                // Optionally, update the local state to reflect the change
            } catch (e: Exception) {
                // Handle error
                Log.e("CounselorsContentViewModel", "Error deleting counselor", e)
            }
        }
    }
}
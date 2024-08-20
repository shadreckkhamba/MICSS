package com.project.micss.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CounselorViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _counselors = MutableStateFlow<List<Counselor>>(emptyList())
    val counselors: StateFlow<List<Counselor>> = _counselors

    init {
        fetchCounselors()
    }

    private fun fetchCounselors() {
        firestore.collection("counselors")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val counselorList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Counselor::class.java)?.copy(id = doc.id)
                }
                _counselors.value = counselorList
            }
    }

    fun addCounselor(counselor: Counselor) = viewModelScope.launch {
        firestore.collection("counselors").add(counselor)
    }

    fun verifyCounselor(counselorId: String, isVerified: Boolean) = viewModelScope.launch {
        firestore.collection("counselors").document(counselorId)
            .update("isVerified", isVerified)
    }

    fun editCounselor(counselor: Counselor) = viewModelScope.launch {
        firestore.collection("counselors").document(counselor.id)
            .set(counselor)
    }

    fun deleteCounselor(counselorId: String) = viewModelScope.launch {
        firestore.collection("counselors").document(counselorId).delete()
    }
}

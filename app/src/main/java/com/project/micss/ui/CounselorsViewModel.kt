package com.project.micss.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Counselor(
    val name: String = "",
    val expertise: List<String> = emptyList(),
    val profilePictureUrl: String = "",
    val availability: List<String> = emptyList(),
    val contactDetails: String = "",
    val phone: String = "",
    val district: String = "",
    val email: String = ""
)

class CounselorsViewModel : ViewModel() {
    private val _counselors = MutableStateFlow<List<Counselor>>(emptyList())
    val counselors: StateFlow<List<Counselor>> = _counselors

    private val firestore = FirebaseFirestore.getInstance()

    init {
        fetchCounselors()
    }

    private fun fetchCounselors() {
        viewModelScope.launch {
            firestore.collection("counselors").get().addOnSuccessListener { result ->
                val counselorList = result.mapNotNull { document ->
                    document.toObject<Counselor>()
                }
                _counselors.value = counselorList
                Log.d("CounselorsViewModel", "Fetched counselors: $counselorList")
            }.addOnFailureListener { exception ->
                Log.e("CounselorsViewModel", "Error fetching counselors: ", exception)
            }
        }
    }
}
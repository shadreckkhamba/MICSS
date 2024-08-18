package com.project.micss.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminLoginViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authResult = MutableStateFlow<Boolean?>(null)
    val authResult: StateFlow<Boolean?> get() = _authResult

    private var username = ""
    private var password = ""

    // Function to clear fields after logging out
    fun clearField(): String = ""

    // Function to set credentials
    fun setCredentials(email: String, password: String) {
        this.username = email
        this.password = password
    }

    fun loginAdmin(username: String, password: String) {
        viewModelScope.launch {
            db.collection("admins")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val adminDoc = documents.documents[0]
                        val storedPassword = adminDoc.getString("password")
                        _authResult.value = (storedPassword == password)
                    } else {
                        _authResult.value = false
                    }
                }
                .addOnFailureListener {
                    _authResult.value = false
                }
        }
    }

    fun resetAuthResult() {
        _authResult.value = null
    }

    fun logoutAdmin() {
        _authResult.value = null
        username = ""
        password = ""
    }
}
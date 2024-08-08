package com.project.micss.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminLoginViewModel : ViewModel() {
    private val _authResult = MutableStateFlow<Boolean?>(null)
    val authResult: StateFlow<Boolean?> get() = _authResult

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginAdmin(email: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _authResult.value = task.isSuccessful
                }
                .addOnFailureListener {
                    _authResult.value = false
                }
        }
    }
}
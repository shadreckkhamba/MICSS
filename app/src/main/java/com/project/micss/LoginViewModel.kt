package com.project.micss

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _authResult = MutableStateFlow<Result<Unit>?>(null)
    val authResult: StateFlow<Result<Unit>?> get() = _authResult

    fun loginUser(auth: FirebaseAuth, email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _authResult.value = Result.success(Unit)
                        } else {
                            val exception = task.exception
                            _authResult.value = Result.failure(
                                exception ?: Exception("Login failed")
                            )
                        }
                    }
            } catch (e: Exception) {
                _authResult.value = Result.failure(e)
            }
        }
    }

    fun signUpUser(auth: FirebaseAuth, email: String, password: String, phoneNumber: String, district: String, area: String, sex: String, age: String) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val userId = user?.uid
                            if (userId != null) {
                                val db = FirebaseFirestore.getInstance()
                                val userData = hashMapOf(
                                    "phoneNumber" to phoneNumber,
                                    "district" to district,
                                    "area" to area,
                                    "sex" to sex,
                                    "age" to age
                                )
                                db.collection("users").document(userId)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        _authResult.value = Result.success(Unit)
                                    }
                                    .addOnFailureListener { e ->
                                        _authResult.value = Result.failure(e)
                                    }
                            } else {
                                _authResult.value = Result.failure(Exception("User ID is null"))
                            }
                        } else {
                            val exception = task.exception
                            _authResult.value = Result.failure(
                                exception ?: Exception("Sign up failed")
                            )
                        }
                    }
            } catch (e: Exception) {
                _authResult.value = Result.failure(e)
            }
        }
    }

    // Optional: Clear auth result after handling
    fun clearAuthResult() {
        _authResult.value = null
    }
}
// FirebaseUtils.kt
package com.project.micss.ui

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

object FirebaseUtils {
    private const val TAG = "FirebaseUtils"

    // Function to upload image to Firebase Storage
    fun uploadImageToFirebaseStorage(fileUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        imagesRef.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get download URL
                imagesRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        // Invoke success callback with download URL
                        onSuccess.invoke(downloadUrl)
                    }
                    .addOnFailureListener { exception ->
                        // Handle failed to get download URL
                        onFailure.invoke(exception)
                    }
            }
            .addOnFailureListener { exception ->
                // Handle unsuccessful upload
                onFailure.invoke(exception)
            }
    }

    // Function to retrieve download URL for a specific file
    fun getDownloadUrl(filePath: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val fileRef = storageRef.child(filePath)

        fileRef.downloadUrl
            .addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                onSuccess.invoke(downloadUrl)
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }


    // Function to store image URL in Firestore (if needed)
    fun storeImageUrlInFirestore(counselorId: String, downloadUrl: String) {
        // Add Firestore update logic here if necessary
        Log.d(TAG, "Storing image URL in Firestore for counselor: $counselorId")
    }
}

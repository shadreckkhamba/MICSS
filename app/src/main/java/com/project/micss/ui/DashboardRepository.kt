package com.project.micss.ui

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class DashboardRepository {
    private val firestore = FirebaseFirestore.getInstance()

    // Function to log detailed information about fetched snapshots
    private fun logSnapshotDetails(collectionName: String, snapshot: QuerySnapshot?, error: Exception?) {
        if (error != null) {
            Log.e("DashboardRepository", "Error fetching $collectionName: ${error.message}")
        } else if (snapshot != null) {
            Log.d("DashboardRepository", "$collectionName count: ${snapshot.size()}")
            snapshot.documents.forEach { document ->
                Log.d("DashboardRepository", "$collectionName document ID: ${document.id}, Data: ${document.data}")
            }
        }
    }

    fun getCounselorsCount(): Flow<Int> = callbackFlow {
        val listener = firestore.collection("counselors")
            .addSnapshotListener { snapshot, error ->
                logSnapshotDetails("counselors", snapshot, error)
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0).isSuccess
            }
        awaitClose { listener.remove() }
    }

    fun getClientsCount(): Flow<Int> = callbackFlow {
        val listener = firestore.collection("clients")
            .addSnapshotListener { snapshot, error ->
                logSnapshotDetails("clients", snapshot, error)
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0).isSuccess
            }
        awaitClose { listener.remove() }
    }

    fun getScheduledAppointmentsCount(): Flow<Int> = callbackFlow {
        val listener = firestore.collection("appointments")
            .addSnapshotListener { snapshot, error ->
                logSnapshotDetails("appointments", snapshot, error)
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0).isSuccess
            }
        awaitClose { listener.remove() }
    }

    fun getCanceledAppointmentsCount(): Flow<Int> = callbackFlow {
        val listener = firestore.collection("appointments")
            .whereEqualTo("status", "canceled")
            .addSnapshotListener { snapshot, error ->
                logSnapshotDetails("canceled appointments", snapshot, error)
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0).isSuccess
            }
        awaitClose { listener.remove() }
    }

    fun getCompletedAppointmentsCount(): Flow<Int> = callbackFlow {
        val listener = firestore.collection("appointments")
            .whereEqualTo("status", "completed")
            .addSnapshotListener { snapshot, error ->
                logSnapshotDetails("completed appointments", snapshot, error)
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0).isSuccess
            }
        awaitClose { listener.remove() }
    }

    fun getResourcesCount(): Flow<Int> = callbackFlow {
        val listener = firestore.collection("resources")
            .addSnapshotListener { snapshot, error ->
                logSnapshotDetails("resources", snapshot, error)
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0).isSuccess
            }
        awaitClose { listener.remove() }
    }

    fun getStakeholdersCount(): Flow<Int> = callbackFlow {
        val listener = firestore.collection("stakeholders")
            .addSnapshotListener { snapshot, error ->
                logSnapshotDetails("stakeholders", snapshot, error)
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0).isSuccess
            }
        awaitClose { listener.remove() }
    }

    fun getUsersCount(): Flow<Int> = callbackFlow {
        val listener = firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                logSnapshotDetails("users", snapshot, error)
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0).isSuccess
            }
        awaitClose { listener.remove() }
    }

    // Combine all the user counts to get the total count
    fun getTotalUsersCount(): Flow<Int> = flow {
        combine(
            getCounselorsCount(),
            getClientsCount(),
            getStakeholdersCount()
        ) { counselorsCount, clientsCount, stakeholdersCount -> counselorsCount + clientsCount + stakeholdersCount

        }.collect { totalUsersCount ->
            emit(totalUsersCount)
        }
    }
}
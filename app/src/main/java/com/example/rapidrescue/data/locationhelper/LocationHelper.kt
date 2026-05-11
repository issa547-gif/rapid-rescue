package com.example.rapidrescue.data.locationhelper

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(context: Context): Pair<Double, Double> {
    val client = LocationServices.getFusedLocationProviderClient(context)
    val cancellationToken = CancellationTokenSource()

    return suspendCoroutine { continuation ->
        // try current location first
        client.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationToken.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                continuation.resume(Pair(location.latitude, location.longitude))
            } else {
                // fallback to last known location
                client.lastLocation
                    .addOnSuccessListener { lastLocation ->
                        if (lastLocation != null) {
                            continuation.resume(
                                Pair(lastLocation.latitude, lastLocation.longitude)
                            )
                        } else {
                            continuation.resumeWithException(
                                Exception("Location unavailable. Enable GPS and try again.")
                            )
                        }
                    }
                    .addOnFailureListener { e ->
                        continuation.resumeWithException(e)
                    }
            }
        }.addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }
    }
}
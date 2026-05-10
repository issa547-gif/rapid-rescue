package com.example.rapidrescue.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlertModel(
    val id: String = "",
    @SerialName("user_id")
    val userId: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val address: String = "",
    val status: String = "sent",
    @SerialName("created_at")
    val createdAt: String = ""
)
package com.example.rapidrescue.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String = "",
    @SerialName("full_name")
    val fullName: String = "",
    @SerialName("blood_type")
    val bloodType: String = "",
    val allergies: String = "",
    val medications: String = "",
    @SerialName("emergency_notes")
    val emergencyNotes: String = ""
)
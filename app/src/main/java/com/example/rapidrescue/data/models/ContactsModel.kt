package com.example.rapidrescue.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: String = "",
    @SerialName("user_id")
    val userId: String = "",
    val name: String = "",
    val phone: String = "",
    val relationship: String = "",
    @SerialName("is_guardian")
    val isGuardian: Boolean = false
)
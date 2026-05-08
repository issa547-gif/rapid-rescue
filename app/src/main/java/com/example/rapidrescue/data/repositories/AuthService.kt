package com.example.rapidrescue.data.repositories

import com.example.rapidrescue.data.models.UserModel

interface AuthService {
    suspend fun insertAlert(lat: Double, lng: Double)
    suspend fun registerUser(user: UserModel)
    suspend fun loginUser(user: UserModel)
    suspend fun resetPassword(email: String)
    suspend fun getUserProfile(user: UserModel)
    suspend fun logoutUser()
}
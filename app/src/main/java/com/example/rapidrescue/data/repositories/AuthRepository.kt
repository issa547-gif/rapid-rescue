package com.example.rapidrescue.data.repositories

import com.example.rapidrescue.data.models.UserModel
import com.example.rapidrescue.data.superbaseclient.SupabaseClientProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class AlertInsert(
    val user_id: String,
    val lat: Double,
    val lng: Double
)

class AuthRepository : AuthService {

    internal val supabase = SupabaseClientProvider.client

    override suspend fun registerUser(user: UserModel) {
        supabase.auth.signUpWith(Email) {
            email = user.email
            password = user.password
            data = buildJsonObject {
                put("full_name", user.name)
            }
        }
    }

    override suspend fun loginUser(user: UserModel) {
        supabase.auth.signInWith(Email) {
            email = user.email
            password = user.password
        }
    }

    override suspend fun resetPassword(email: String) {
        supabase.auth.resetPasswordForEmail(email = email)
    }

    override suspend fun getUserProfile(user: UserModel) {}

    override suspend fun logoutUser() {
        supabase.auth.signOut()
    }

    override suspend fun insertAlert(lat: Double, lng: Double) {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw Exception("User not authenticated")
        supabase.from("alerts").insert(
            AlertInsert(user_id = userId, lat = lat, lng = lng)
        )
    }
}
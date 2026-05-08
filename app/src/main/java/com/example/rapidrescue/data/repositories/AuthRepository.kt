package com.example.rapidrescue.data.repositories

import com.example.rapidrescue.data.models.UserModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

class AuthRepository : AuthService {

    private val supabase = createSupabaseClient(
        supabaseUrl = "https://izjioxgbwamfahgpgjbs.supabase.co",
        supabaseKey = "sb_publishable_DSw74uObt_AgYzwsmWLLkg_JzOdIW61"
    ) {
        install(Postgrest)
        install(Auth)
    }
    override suspend fun insertAlert(lat: Double, lng: Double) {
        supabase.from("alerts").insert(
            mapOf("lat" to lat, "lng" to lng)
        )
    }
    override suspend fun registerUser(user: UserModel) {
        supabase.auth.signUpWith(Email) {
            email = user.email
            password = user.password
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

    override suspend fun getUserProfile(user: UserModel) {
        // implement when profile screen is ready
    }

    override suspend fun logoutUser() {
        supabase.auth.signOut()
    }
}
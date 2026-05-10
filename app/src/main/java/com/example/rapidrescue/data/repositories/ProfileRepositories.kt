package com.example.rapidrescue.data.repositories

import com.example.rapidrescue.data.models.Profile
import com.example.rapidrescue.data.superbaseclient.SupabaseClientProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class ProfileRepository {

    private val supabase = SupabaseClientProvider.client

    fun getCurrentUserEmail(): String {
        return supabase.auth.currentUserOrNull()?.email ?: ""
    }

    fun getCurrentUserId(): String {
        return supabase.auth.currentUserOrNull()?.id ?: ""
    }

    suspend fun getProfile(): Profile? {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return null
        return supabase.from("profiles")
            .select {
                filter { eq("id", userId) }
            }
            .decodeSingleOrNull<Profile>()
    }

    suspend fun updateProfile(profile: Profile) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return
        supabase.from("profiles").upsert(
            profile.copy(id = userId)
        ) {
            onConflict = "id"
        }
    }
}
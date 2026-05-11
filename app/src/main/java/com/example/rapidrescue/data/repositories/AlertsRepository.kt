package com.example.rapidrescue.data.repositories

import com.example.rapidrescue.data.SupabaseClientProvider
import com.example.rapidrescue.data.models.AlertModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class AlertRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun getAlerts(): List<AlertModel> {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw Exception("User not authenticated")
        return supabase.from("alerts")
            .select {
                filter { eq("user_id", userId) }
                order("created_at", order = Order.DESCENDING)
            }
            .decodeList<AlertModel>()
    }
}
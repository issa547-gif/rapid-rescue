package com.example.rapidrescue.data.superbaseclient

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {
    val client = createSupabaseClient(
        supabaseUrl = "https://izjioxgbwamfahgpgjbs.supabase.co",
        supabaseKey = "sb_publishable_DSw74uObt_AgYzwsmWLLkg_JzOdIW61"
    ) {
        install(Postgrest)
        install(Auth)
    }
}
package com.example.rapidrescue.data.repositories

import com.example.rapidrescue.data.models.Contact
import com.example.rapidrescue.data.superbaseclient.SupabaseClientProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class ContactRepository {

    private val supabase = SupabaseClientProvider.client

    private fun currentUserId(): String {
        return supabase.auth.currentUserOrNull()?.id
            ?: throw Exception("User not authenticated")
    }

    suspend fun getContacts(): List<Contact> {
        val userId = currentUserId()
        return supabase.from("contacts")
            .select {
                filter { eq("user_id", userId) }
            }
            .decodeList<Contact>()
    }

    suspend fun addContact(contact: Contact) {
        val userId = currentUserId()
        supabase.from("contacts").insert(
            contact.copy(userId = userId)
        )
    }

    suspend fun deleteContact(id: String) {
        supabase.from("contacts").delete {
            filter { eq("id", id) }
        }
    }
}
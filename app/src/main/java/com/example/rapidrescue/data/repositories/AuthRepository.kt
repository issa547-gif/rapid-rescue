package com.example.rapidrescue.data.repositories

import com.example.rapidrescue.data.models.UserModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

class AuthRepository: AuthService {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://izjioxgbwamfahgpgjbs.supabase.co/rest/v1/",
        supabaseKey = "sb_publishable_DSw74uObt_AgYzwsmWLLkg_JzOdIW61"
    )  {
        install(Postgrest)
        install(Auth)
    }


    override suspend fun registerUser(userDetails: UserModel)  {
        supabase.auth.signUpWith(Email) {
            email = userDetails.email
            password = userDetails.password
        }
    }

    override suspend fun loginUser(userDetails: UserModel)  {
        val user = supabase.auth.signInWith(Email) {
            email = userDetails.email
            password = userDetails.password
        }
    }

    override suspend fun resetPassword(email: String) {
        supabase.auth.resetPasswordForEmail(email = email)
    }

    override suspend fun getUserProfile(user: UserModel) {
//        TODO("Not yet implemented")
    }

    override suspend fun logoutUser() {
        supabase.auth.signOut()
    }

}
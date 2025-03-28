package ru.madfinal.launguageapp.data.auth

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.madfinal.lastweeksproject.data.datasource.network.config.NetworkConfig
import ru.madfinal.launguageapp.data.auth.models.AuthRequest
import ru.madfinal.launguageapp.data.auth.models.AuthResponse
import ru.madfinal.launguageapp.data.auth.models.SignupRequest
import ru.madfinal.launguageapp.data.datasource.network.supabase.SupabaseApi

class AuthDataSource(private val api: SupabaseApi) {
    fun login(email: String, password: String): Single<AuthResponse> {
        val request = AuthRequest(email, password)
        return api.login(request, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1uZ2Npc3lqbXZibWVibmxhcGh0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzAxOTAyMDIsImV4cCI6MjA0NTc2NjIwMn0.MFGhx41ZSshllA0x677E_sy3nFOcjZG-HFuTNE8yBz4")
    }

    fun logout(token: String): Completable {
        val authToken = "${NetworkConfig.AUTH_PREFIX}$token"
        return api.logout(authToken)
    }

    fun signUp(
        email: String,
        password: String,
        name: String,
        lastName: String
    ): Single<AuthResponse> {
        val request = SignupRequest(email, name, lastName, password)
        return api.signup(request)
    }
}
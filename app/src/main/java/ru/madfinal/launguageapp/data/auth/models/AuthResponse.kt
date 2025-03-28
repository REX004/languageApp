package ru.madfinal.launguageapp.data.auth.models

data class AuthResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String,
    val user: UserDto
)

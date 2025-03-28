package ru.madfinal.launguageapp.data.auth.models

data class SignupRequest(
    val email: String,
    val name: String,
    val lastName: String,
    val password: String
)
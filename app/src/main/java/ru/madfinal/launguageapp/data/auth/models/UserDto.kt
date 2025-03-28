package ru.madfinal.launguageapp.data.auth.models

data class UserDto(
    val id: String,
    val email: String,
    val name: String? = null,
    val created_at: String? = null
)
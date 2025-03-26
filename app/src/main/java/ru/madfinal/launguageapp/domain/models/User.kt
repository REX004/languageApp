package ru.madfinal.launguageapp.domain.models

data class User(
    val id: Int? = null,
    val nickName: String? = null,
    val name: String? = null,
    val lastName: String? = null,
    val age: Int? = null,
    val email: String? = null,
    val password: String? = null,
    val score: String? = null,
    val image: String? = null
)

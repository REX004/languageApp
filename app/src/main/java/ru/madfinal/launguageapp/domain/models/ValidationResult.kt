package ru.madfinal.launguageapp.domain.models

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String = ""
)
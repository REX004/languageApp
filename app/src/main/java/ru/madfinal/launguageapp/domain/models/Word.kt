package ru.madfinal.launguageapp.domain.models

data class Word(
    val id: Int,
    val english: String,
    val russian: String,
    val transcription_english: String,
    val transcription_russian: String
)
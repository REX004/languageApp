package ru.madfinal.launguageapp.data.datasource.network.supabase

interface TokenProvider {
    fun getAccessToken(): String
}
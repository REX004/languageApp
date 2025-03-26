package ru.madfinal.launguageapp.data.datasource.network.supabase

sealed class ResponseState<T> {
    class Success<T>(val data: T) : ResponseState<T>() {}
    class Error<T>(var message: String) : ResponseState<T>() {}
    class Loading<T>() : ResponseState<T>()
}
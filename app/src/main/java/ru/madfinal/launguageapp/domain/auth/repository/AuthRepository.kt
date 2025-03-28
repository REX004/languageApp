package ru.madfinal.launguageapp.domain.auth.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.domain.models.User

interface AuthRepository {
    fun login(email: String, password: String): Single<User>
    fun logout(): Completable
    fun isUserLoggedIn(): Boolean
    fun signUp(email: String, password: String, name: String, lastName: String): Single<User>
}
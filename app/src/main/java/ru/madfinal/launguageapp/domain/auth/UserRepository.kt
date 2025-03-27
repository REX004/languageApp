package ru.madfinal.launguageapp.domain.auth


import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.domain.models.User

interface UserRepository {
    fun updateUserScore(newScore: Int): Completable
    fun getUserScore(): Single<Int>
    fun getCurrentUser(): Single<User>
    fun getTopUsers(limit: Int = 3): Single<List<User>>
}
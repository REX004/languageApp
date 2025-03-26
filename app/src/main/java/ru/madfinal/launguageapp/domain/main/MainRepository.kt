package ru.madfinal.launguageapp.domain.main

import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.domain.models.User

interface MainRepository {
    fun getTopUsers(): Single<List<User>>
}
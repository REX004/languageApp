package ru.madfinal.launguageapp.domain.exercise.game.repository

import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.domain.models.Word

interface GameRepository {
    fun getUserWords(): Single<List<Word>>
}
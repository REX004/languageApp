package ru.madfinal.launguageapp.domain.exercise.word


import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.domain.models.Word

interface WordRepository {

    fun getWords(): Single<List<Word>>
    fun getRandomWord(): Single<Word>
}
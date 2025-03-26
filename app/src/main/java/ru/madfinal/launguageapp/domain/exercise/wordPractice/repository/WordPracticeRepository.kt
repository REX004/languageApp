package ru.madfinal.launguageapp.domain.exercise.wordPractice.repository

import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.domain.models.Word

interface WordPracticeRepository {
    fun getWords(): Single<List<Word>>
}
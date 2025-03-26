package ru.madfinal.launguageapp.data.exercise.wordPractice

import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.data.datasource.network.supabase.SupabaseApi
import ru.madfinal.launguageapp.domain.exercise.wordPractice.repository.WordPracticeRepository
import ru.madfinal.launguageapp.domain.models.Word

class WordPracticeRepositoryImpl(private val api: SupabaseApi, private val apiKey: String) :
    WordPracticeRepository {

    override fun getWords(): Single<List<Word>> {
        return api.getWords(apiKey)
    }
}
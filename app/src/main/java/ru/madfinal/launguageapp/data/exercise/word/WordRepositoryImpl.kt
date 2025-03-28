package ru.madfinal.launguageapp.data.exercise.word


import io.reactivex.rxjava3.core.Single
import ru.madfinal.lastweeksproject.data.datasource.network.config.NetworkConfig
import ru.madfinal.launguageapp.data.datasource.network.supabase.SupabaseApi
import ru.madfinal.launguageapp.domain.exercise.word.WordRepository
import ru.madfinal.launguageapp.domain.models.Word
import java.util.Random

class WordRepositoryImpl(private val api: SupabaseApi) : WordRepository {

    private val random = Random()

    override fun getWords(): Single<List<Word>> {
        return api.getWords(apiKey = NetworkConfig.API_KEY)
    }

    override fun getRandomWord(): Single<Word> {
        return getWords()
            .map { words ->
                if (words.isEmpty()) {
                    throw NoSuchElementException("Словарь пуст")
                }
                words[random.nextInt(words.size)]
            }
    }
}
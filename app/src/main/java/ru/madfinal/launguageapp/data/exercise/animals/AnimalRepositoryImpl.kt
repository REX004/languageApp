package ru.madfinal.launguageapp.data.exercise.animals

import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.data.datasource.network.supabase.SupabaseApi
import ru.madfinal.launguageapp.domain.exercise.animals.repository.AnimalRepository
import ru.madfinal.launguageapp.domain.models.Animal

class AnimalRepositoryImpl(
    private val supabaseApi: SupabaseApi,
    private val apiKey: String
) : AnimalRepository {

    override fun getRandomAnimal(): Single<Animal> {
        // Получаем случайное животное из базы данных
        return supabaseApi.getAnimal(
            apiKey = apiKey,
            fields = "*"
        ).map { animal ->
            // Если API возвращает список, берем случайное животное
            if (animal is List<Animal>) {
                (animal as List<Animal>).random()
            } else {
                // Если API возвращает одно животное, используем его
                animal as Animal
            }
        }
    }
}
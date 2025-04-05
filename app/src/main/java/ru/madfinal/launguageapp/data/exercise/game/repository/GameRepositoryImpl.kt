package ru.madfinal.launguageapp.data.exercise.game.repository

import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.data.datasource.network.supabase.SupabaseApi
import ru.madfinal.launguageapp.domain.exercise.game.repository.GameRepository
import ru.madfinal.launguageapp.domain.models.Word

class GameRepositoryImpl(
    private val supabaseApi: SupabaseApi
) : GameRepository {
    override fun getUserWords(): Single<List<Word>> {
        return supabaseApi.getWords(
            ""
        )
    }
}
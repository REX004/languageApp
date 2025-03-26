package ru.madfinal.launguageapp.data.main.repository


import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.data.datasource.network.supabase.SupabaseApi
import ru.madfinal.launguageapp.domain.main.MainRepository
import ru.madfinal.launguageapp.domain.models.User

class MainRepositoryImpl(private val api: SupabaseApi, private val apiKey: String) : MainRepository {

    override fun getTopUsers(): Single<List<User>> {
        return api.getTopUsers(apiKey)
    }
}
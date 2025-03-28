package ru.madfinal.launguageapp.data.main.user

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.madfinal.lastweeksproject.data.datasource.network.config.NetworkConfig
import ru.madfinal.launguageapp.data.datasource.locale.UserPreferences
import ru.madfinal.launguageapp.data.datasource.network.supabase.SupabaseApi
import ru.madfinal.launguageapp.domain.auth.UserRepository
import ru.madfinal.launguageapp.domain.models.User

class UserRepositoryImpl(
    private val api: SupabaseApi,
    private val userPreferences: UserPreferences
) : UserRepository {

    override fun updateUserScore(newScore: Int): Completable {
        val userId = userPreferences.getUserId()

        if (userId.isEmpty()) {
            // Если ID пользователя не найден, просто сохраняем счет локально
            userPreferences.saveUserScore(newScore)
            return Completable.complete()
        }

        // Обновляем счет на сервере
        val scoreUpdate = mapOf("score" to newScore)

        return api.updateUserScore(
            apiKey = NetworkConfig.API_KEY,
            userId = userId,
            scoreUpdate = scoreUpdate
        ).doOnComplete {
            // После успешного обновления на сервере, обновляем локальный счет
            userPreferences.saveUserScore(newScore)
        }
    }

    override fun getUserScore(): Single<Int> {
        val localScore = userPreferences.getUserScore()

        // Если есть локальный счет, возвращаем его
        return Single.just(localScore)

        // Альтернативно, можно запросить счет с сервера:
        // val userId = userPreferences.getUserId()
        // if (userId.isEmpty()) {
        //     return Single.just(localScore)
        // }
        //
        // return api.getUserById(apiKey = NetworkConfig.API_KEY, userId = userId)
        //     .map { user -> user.score }
        //     .doOnSuccess { serverScore ->
        //         userPreferences.saveUserScore(serverScore)
        //     }
        //     .onErrorReturn { localScore }
    }

    override fun getCurrentUser(): Single<User> {
        val userId = userPreferences.getUserId()

        if (userId.isEmpty()) {
            return Single.error(IllegalStateException("Пользователь не авторизован"))
        }

        // Здесь нужно реализовать запрос к API для получения данных пользователя
        // Пример (предполагается, что вы добавите метод getUserById в SupabaseApi):
        return api.getUserById(
            apiKey = NetworkConfig.API_KEY,
            userId = userId
        ).map { users ->
            if (users.isEmpty()) {
                throw NoSuchElementException("Пользователь не найден")
            }
            users.first()
        }
    }

    override fun getTopUsers(limit: Int): Single<List<User>> {
        return api.getTopUsers(
            apiKey = NetworkConfig.API_KEY,
            limit = limit
        )
    }
}
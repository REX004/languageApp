package ru.madfinal.launguageapp.data.auth.repository

import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.data.auth.AuthDataSource
import ru.madfinal.launguageapp.data.datasource.network.supabase.TokenProvider
import ru.madfinal.launguageapp.data.mapper.UserMapper
import ru.madfinal.launguageapp.domain.auth.repository.AuthRepository
import ru.madfinal.launguageapp.domain.models.User

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val userMapper: UserMapper,
    private val preferences: SharedPreferences
) : AuthRepository, TokenProvider {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
    }

    override fun login(email: String, password: String): Single<User> {
        return authDataSource.login(email, password)
            .map { response ->
                saveUserData(response.access_token, response.user.id, response.user.email)
                userMapper.mapToDomain(response.user)
            }
    }

    override fun logout(): Completable {
        val token = getAccessToken()
        return if (token.isEmpty()) {
            Completable.complete()
        } else {

            authDataSource.logout(token)
                .doOnComplete { clearUserData() }
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return preferences.contains(KEY_ACCESS_TOKEN)
    }

    override fun signUp(
        email: String,
        password: String,
        name: String,
        lastName: String
    ): Single<User> {
        return authDataSource.signUp(email, password, name, lastName)
            .map { response ->
            saveUserData(response.access_token, response.user.id, response.user.email)
            userMapper.mapToDomain(response.user)
        }
    }

    override fun getAccessToken(): String {
        return preferences.getString(KEY_ACCESS_TOKEN, "") ?: ""
    }

    private fun saveUserData(token: String, userId: String, email: String) {
        preferences.edit().apply {
            putString(KEY_ACCESS_TOKEN, token)
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_EMAIL, email)
            apply()
        }
    }

    private fun clearUserData() {
        preferences.edit().clear().apply()
    }
}
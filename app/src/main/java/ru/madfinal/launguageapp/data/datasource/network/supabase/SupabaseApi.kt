package ru.madfinal.launguageapp.data.datasource.network.supabase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*
import ru.madfinal.lastweeksproject.data.datasource.network.config.NetworkConfig
import ru.madfinal.launguageapp.data.auth.models.AuthRequest
import ru.madfinal.launguageapp.data.auth.models.AuthResponse
import ru.madfinal.launguageapp.data.auth.models.SignupRequest
import ru.madfinal.launguageapp.data.auth.models.UserDto
import ru.madfinal.launguageapp.data.auth.models.UserUpdateDto
import ru.madfinal.launguageapp.domain.models.Animal
import ru.madfinal.launguageapp.domain.models.User
import ru.madfinal.launguageapp.domain.models.Word


interface SupabaseApi {
    @POST("auth/v1/token?grant_type=${NetworkConfig.GRANT_TYPE_PASSWORD}")
    fun login(
        @Body request: AuthRequest,
        @Header("apiKey") authToken: String,
    ): Single<AuthResponse>

    @POST("auth/v1/token?grant_type=${NetworkConfig.GRANT_TYPE_PASSWORD}")
    fun signup(
        @Body request: SignupRequest,
        @Header("apiKey") apiKey: String
    ): Single<AuthResponse>

    @POST("auth/v1/logout")
    fun logout(@Header("Authorization") authToken: String): Completable

    @GET("rest/v1/users")
    fun getUsers(
        @Header("Authorization") authToken: String,
        @Query("select") fields: String = "*"
    ): Single<List<UserDto>>

    @POST("rest/v1/users")
    fun createUser(
        @Header("Authorization") authToken: String,
        @Header("Prefer") prefer: String = "return=representation",
        @Body user: UserDto
    ): Single<List<UserDto>>

    @PATCH("rest/v1/users")
    fun updateUser(
        @Header("Authorization") authToken: String,
        @Header("Prefer") prefer: String = "return=representation",
        @Query("id") id: String,
        @Body user: UserUpdateDto
    ): Single<List<UserDto>>

    @DELETE("rest/v1/users")
    fun deleteUser(
        @Header("Authorization") authToken: String,
        @Query("id") id: String
    ): Completable

    @GET("rest/v1/users")
    fun getTopUsers(
        @Header("apiKey") apiKey: String,
        @Query("order") order: String = "score.desc",
        @Query("limit") limit: Int = 3
    ): Single<List<User>>

    @GET("rest/v1/word_practice")
    fun getWords(
        @Header("apiKey") apiKey: String,
        @Query("select") fields: String = "id,english,russian,transcription_english" // Выбираем все нужные поля
    ): Single<List<Word>>

    @GET("rest/v1/animals")
    fun getAnimal(
        @Header("apiKey") apiKey: String,
        @Query("select") fields: String = "*"
    ): Single<List<Animal>>


    @GET("rest/v1/users")
    fun getUserById(
        @Header("apiKey") apiKey: String,
        @Query("id") userId: String,
        @Query("select") fields: String = "*"
    ): Single<List<User>>


    @PATCH("rest/v1/users")
    fun updateUserScore(
        @Header("apiKey") apiKey: String,
        @Query("id") userId: String,
        @Body scoreUpdate: Map<String, Int>
    ): Completable

    @GET("rest/v1/game")
    fun getGameWords(
        @Header("apiKey") apiKey: String,
        @Query("select") fields: String = "id,english,russian,transcription_english"
    ): Single<List<Word>>
}
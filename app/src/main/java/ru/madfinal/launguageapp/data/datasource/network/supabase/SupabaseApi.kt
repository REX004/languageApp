package ru.madfinal.launguageapp.data.datasource.network.supabase

import io.reactivex.rxjava3.core.Single
import retrofit2.http.*
import ru.madfinal.launguageapp.domain.models.User
import ru.madfinal.launguageapp.domain.models.Word


interface SupabaseApi {
//    @POST("auth/v1/token?grant_type=${NetworkConfig.GRANT_TYPE_PASSWORD}")
//    fun login(@Body request: AuthRequest): Single<AuthResponse>
//
//    @POST("auth/v1/logout")
//    fun logout(@Header("Authorization") authToken: String): Completable

//    @GET("rest/v1/users")
//    fun getUsers(
//        @Header("Authorization") authToken: String,
//        @Query("select") fields: String = "*"
//    ): Single<List<UserDto>>
//
//    @GET("rest/v1/users")
//    fun getUserById(
//        @Header("Authorization") authToken: String,
//        @Query("id") id: String,
//        @Query("select") fields: String = "*"
//    ): Single<List<UserDto>>
//
//    @POST("rest/v1/users")
//    fun createUser(
//        @Header("Authorization") authToken: String,
//        @Header("Prefer") prefer: String = "return=representation",
//        @Body user: UserDto
//    ): Single<List<UserDto>>
//
//    @PATCH("rest/v1/users")
//    fun updateUser(
//        @Header("Authorization") authToken: String,
//        @Header("Prefer") prefer: String = "return=representation",
//        @Query("id") id: String,
//        @Body user: UserUpdateDto
//    ): Single<List<UserDto>>
//
//    @DELETE("rest/v1/users")
//    fun deleteUser(
//        @Header("Authorization") authToken: String,
//        @Query("id") id: String
//    ): Completable

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
}
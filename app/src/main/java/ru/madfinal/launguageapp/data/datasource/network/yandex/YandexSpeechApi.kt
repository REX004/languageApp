package ru.madfinal.launguageapp.data.datasource.network.yandex

import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface YandexSpeechApi {
    @POST("speech/v1/stt:recognize")
    @Multipart
    fun recognizeSpeech(
        @Part audioFile: MultipartBody.Part,
        @Query("lang") language: String = "en-US",
        @Query("format") format: String = "lpcm",
        @Query("sampleRateHertz") sampleRate: Int = 16000
    ): Single<SpeechRecognitionResponse>
}

data class SpeechRecognitionResponse(
    val result: String
)
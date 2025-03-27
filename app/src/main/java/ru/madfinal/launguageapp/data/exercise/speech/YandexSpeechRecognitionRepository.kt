package ru.madfinal.launguageapp.data.exercise.speech

import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.madfinal.launguageapp.data.datasource.network.yandex.YandexSpeechApi
import ru.madfinal.launguageapp.domain.exercise.speech.SpeechRecognitionRepository
import java.io.File

class YandexSpeechRecognitionRepository(
    private val yandexSpeechApi: YandexSpeechApi
) : SpeechRecognitionRepository {

    override fun recognizeSpeech(audioFile: File, language: String): Single<String> {
        val requestFile = RequestBody.create(
            "audio/x-pcm".toMediaTypeOrNull(),
            audioFile
        )

        val body = MultipartBody.Part.createFormData(
            "file",
            audioFile.name,
            requestFile
        )

        return yandexSpeechApi.recognizeSpeech(
            audioFile = body,
            language = language
        ).map { response ->
            response.result
        }
    }
}
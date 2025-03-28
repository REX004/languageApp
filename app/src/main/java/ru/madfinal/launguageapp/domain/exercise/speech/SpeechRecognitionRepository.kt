package ru.madfinal.launguageapp.domain.exercise.speech

import io.reactivex.rxjava3.core.Single
import java.io.File

interface SpeechRecognitionRepository {
    fun recognizeSpeech(audioFile: File, language: String = "en-US"): Single<String>
}
package ru.madfinal.launguageapp.presentation.exercise.animals

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.madfinal.launguageapp.domain.exercise.animals.usecase.GetRandomAnimalUseCase
import ru.madfinal.launguageapp.domain.models.Animal

class AnimalsViewModel(
    private val getRandomAnimalUseCase: GetRandomAnimalUseCase,
    private val imageRecognitionHelper: TensorFlowHelper
) : ViewModel() {

    private val _currentAnimal = MutableLiveData<Animal>()
    val currentAnimal: LiveData<Animal> = _currentAnimal

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int> = _score

    private val _consecutiveCorrectAnswers = MutableLiveData<Int>(0)
    val consecutiveCorrectAnswers: LiveData<Int> = _consecutiveCorrectAnswers

    private val _recognizedAnimal = MutableLiveData<String>()
    val recognizedAnimal: LiveData<String> = _recognizedAnimal

    private val _isAnswerCorrect = MutableLiveData<Boolean>()
    val isAnswerCorrect: LiveData<Boolean> = _isAnswerCorrect

    private val compositeDisposable = CompositeDisposable()

    init {
        loadRandomAnimal()
    }

    fun loadRandomAnimal() {
        _loading.value = true
        compositeDisposable.add(
            getRandomAnimalUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ animal ->
                    _currentAnimal.value = animal
                    _loading.value = false
                }, { throwable ->
                    _error.value = throwable.message ?: "Unknown error"
                    _loading.value = false
                })
        )
    }

    fun checkAnswer(userAnswer: String, animalImage: Bitmap) {
        _loading.value = true

        // Логируем ответ пользователя
        Log.d("AnimalsViewModel", "User answer: $userAnswer")

        // Получаем текущее животное
        val currentAnimal = _currentAnimal.value

        if (currentAnimal == null) {
            _error.value = "No animal loaded"
            _loading.value = false
            return
        }

        // Используем ImageRecognitionHelper для распознавания изображения
        imageRecognitionHelper.recognizeImage(animalImage) { recognizedObject ->
            // Логируем распознанный объект
            Log.d("AnimalsViewModel", "Recognized object: $recognizedObject")

            // Если распознавание не удалось или вернуло "unknown",
            // используем имя животного из объекта Animal
            val actualAnimalName =
                if (recognizedObject == "unknown" || recognizedObject.startsWith("error")) {
                    Log.d(
                        "AnimalsViewModel",
                        "Using animal name from database: ${currentAnimal.name}"
                    )
                    currentAnimal.name
                } else {
                    recognizedObject
                }

            _recognizedAnimal.postValue(actualAnimalName)

            // Нормализуем строки для сравнения
            val normalizedUserAnswer = userAnswer.trim().lowercase()
            val normalizedAnimalName = actualAnimalName.trim().lowercase()

            // Логируем нормализованные строки
            Log.d("AnimalsViewModel", "Normalized user answer: $normalizedUserAnswer")
            Log.d("AnimalsViewModel", "Normalized animal name: $normalizedAnimalName")

            // Более гибкое сравнение ответов
            val isCorrect = isAnswerCorrect(normalizedUserAnswer, normalizedAnimalName)
            Log.d("AnimalsViewModel", "Is answer correct: $isCorrect")

            _isAnswerCorrect.postValue(isCorrect)

            // Обновляем счет
            if (isCorrect) {
                val consecutiveCorrect = (_consecutiveCorrectAnswers.value ?: 0) + 1
                _consecutiveCorrectAnswers.postValue(consecutiveCorrect)

                val additionalScore = if (consecutiveCorrect >= 2) {
                    0.2 * consecutiveCorrect
                } else {
                    0.0
                }

                val currentScore = _score.value ?: 0
                _score.postValue(currentScore + 1 + additionalScore.toInt())
            } else {
                _consecutiveCorrectAnswers.postValue(0)
            }

            _loading.postValue(false)
        }
    }

    private fun isAnswerCorrect(userAnswer: String, animalName: String): Boolean {
        // Прямое сравнение
        if (userAnswer == animalName) {
            return true
        }

        // Проверка, содержит ли ответ пользователя имя животного или наоборот
        if (userAnswer.contains(animalName) || animalName.contains(userAnswer)) {
            return true
        }

        // Добавим немного гибкости: проверка на множественное число
        if (userAnswer.endsWith("s") && userAnswer.dropLast(1) == animalName) {
            return true
        }
        if (animalName.endsWith("s") && animalName.dropLast(1) == userAnswer) {
            return true
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
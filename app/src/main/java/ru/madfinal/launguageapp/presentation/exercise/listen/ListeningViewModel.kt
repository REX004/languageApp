package ru.madfinal.launguageapp.presentation.exercise.listen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.madfinal.launguageapp.domain.auth.UserRepository
import ru.madfinal.launguageapp.domain.exercise.speech.SpeechRecognitionRepository
import ru.madfinal.launguageapp.domain.exercise.word.WordRepository
import ru.madfinal.launguageapp.domain.models.Word
import java.io.File

class ListeningViewModel(
    private val wordRepository: WordRepository,
    private val userRepository: UserRepository,
    private val speechRecognitionRepository: SpeechRecognitionRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _currentWord = MutableLiveData<Word>()
    val currentWord: LiveData<Word> = _currentWord

    private val _recognitionResult = MutableLiveData<String>()
    val recognitionResult: LiveData<String> = _recognitionResult

    private val _isRecognizing = MutableLiveData<Boolean>()
    val isRecognizing: LiveData<Boolean> = _isRecognizing

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _wordsCache = mutableListOf<Word>()
    private val _usedWords = mutableSetOf<Int>()

    init {
        loadWords()
    }

    private fun loadWords() {
        disposables.add(
            wordRepository.getWords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ words ->
                    _wordsCache.clear()
                    _wordsCache.addAll(words)
                    if (_currentWord.value == null) {
                        loadRandomWord()
                    }
                }, { error ->
                    _error.value = "Ошибка загрузки слов: ${error.message}"
                })
        )
    }

    fun loadRandomWord() {
        if (_wordsCache.isEmpty()) {
            return
        }

        if (_usedWords.size >= _wordsCache.size) {
            _usedWords.clear()
        }

        // Выбираем случайное слово, которое еще не использовалось
        val availableWords = _wordsCache.filter { it.id !in _usedWords }
        if (availableWords.isNotEmpty()) {
            val randomWord = availableWords.random()
            _currentWord.value = randomWord
            _usedWords.add(randomWord.id)
        } else {
            // На всякий случай, если фильтрация не сработала
            val randomWord = _wordsCache.random()
            _currentWord.value = randomWord
            _usedWords.add(randomWord.id)
        }
    }

    fun recognizeSpeech(audioFile: File) {
        _isRecognizing.value = true

        disposables.add(
            speechRecognitionRepository.recognizeSpeech(audioFile, "en-US")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    _isRecognizing.value = false
                    _recognitionResult.value = result
                }, { error ->
                    _isRecognizing.value = false
                    _error.value = "Ошибка распознавания речи: ${error.message}"
                })
        )
    }

    fun updateScore(newScore: Int) {
        disposables.add(
            userRepository.updateUserScore(newScore)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Успешное обновление счета
                }, { error ->
                    _error.value = "Ошибка обновления счета: ${error.message}"
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
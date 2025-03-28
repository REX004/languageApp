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

    companion object {
        private const val ERROR_LOAD_WORDS = "Ошибка загрузки слов: %s"
        private const val ERROR_SPEECH_RECOGNITION = "Ошибка распознавания речи: %s"
        private const val ERROR_UPDATE_SCORE = "Ошибка обновления счета: %s"
    }

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
                .subscribe(
                    { words -> handleWordsLoaded(words) },
                    { error -> handleError(ERROR_LOAD_WORDS, error) }
                )
        )
    }

    private fun handleWordsLoaded(words: List<Word>) {
        _wordsCache.clear()
        _wordsCache.addAll(words)
        if (_currentWord.value == null) {
            loadRandomWord()
        }
    }

    fun loadRandomWord() {
        val availableWords = getAvailableWords()
        if (availableWords.isNotEmpty()) {
            val randomWord = availableWords.random()
            _currentWord.value = randomWord
            _usedWords.add(randomWord.id)
        }
    }

    private fun getAvailableWords(): List<Word> {
        if (_wordsCache.isEmpty()) return emptyList()

        // Reset used words if all words have been used
        if (_usedWords.size >= _wordsCache.size) {
            _usedWords.clear()
        }

        return _wordsCache.filter { it.id !in _usedWords }
    }

    fun recognizeSpeech(audioFile: File) {
        _isRecognizing.value = true

        disposables.add(
            speechRecognitionRepository.recognizeSpeech(audioFile, "en-US")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> handleRecognitionResult(result) },
                    { error -> handleRecognitionError(error) }
                )
        )
    }

    private fun handleRecognitionResult(result: String) {
        _isRecognizing.value = false
        _recognitionResult.value = result
    }

    private fun handleRecognitionError(error: Throwable) {
        _isRecognizing.value = false
        handleError(ERROR_SPEECH_RECOGNITION, error)
    }

    fun updateScore(newScore: Int) {
        disposables.add(
            userRepository.updateUserScore(newScore)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { /* Success */ },
                    { error -> handleError(ERROR_UPDATE_SCORE, error) }
                )
        )
    }

    private fun handleError(messageTemplate: String, error: Throwable) {
        _error.value = messageTemplate.format(error.message)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
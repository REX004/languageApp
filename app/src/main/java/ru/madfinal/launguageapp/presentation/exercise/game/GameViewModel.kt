package ru.madfinal.launguageapp.presentation.exercise.game


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.madfinal.launguageapp.domain.exercise.wordPractice.usecase.GetRandomWordUseCase
import ru.madfinal.launguageapp.domain.models.Word

class GameViewModel(private val getRandomWordUseCase: GetRandomWordUseCase) : ViewModel() {

    private val _currentWord = MutableLiveData<Word>()
    val currentWord: LiveData<Word> = _currentWord

    private val _options = MutableLiveData<List<String>>()
    val options: LiveData<List<String>> = _options

    private val _isEnglishTurn =
        MutableLiveData<Boolean>()
    val isEnglishTurn: LiveData<Boolean> = _isEnglishTurn

    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int> = _score

    private val _consecutiveCorrectAnswers = MutableLiveData<Int>(0)
    val consecutiveCorrectAnswers: LiveData<Int> = _consecutiveCorrectAnswers

    private val _selectedOption = MutableLiveData<String?>()
    val selectedOption: LiveData<String?> = _selectedOption

    private val _correctAnswer = MutableLiveData<String?>()
    val correctAnswer: LiveData<String?> = _correctAnswer

    private val _isAnswerChecked = MutableLiveData<Boolean>(false)
    val isAnswerChecked: LiveData<Boolean> = _isAnswerChecked

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    private val compositeDisposable = CompositeDisposable()

    init {
        loadNextWord()
    }

    fun loadNextWord() {
        _loading.value = true
        _selectedOption.value = null
        _correctAnswer.value = null
        _isAnswerChecked.value = false

        compositeDisposable.add(
            getRandomWordUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ (word, options, isEnglishQuestion) ->
                    _currentWord.value = word
                    _options.value = options
                    _isEnglishTurn.value = isEnglishQuestion
                    _loading.value = false
                }, { throwable ->
                    _error.value = throwable.message ?: "Unknown error"
                    _loading.value = false
                })
        )
    }

    fun onOptionSelected(option: String) {
        if (_isAnswerChecked.value == true) return
        _selectedOption.value = option
    }

    fun checkAnswer() {
        if (_selectedOption.value == null) return

        _isAnswerChecked.value = true
        val currentWordValue = _currentWord.value ?: return
        val correctAnswerValue =
            if (_isEnglishTurn.value == true) currentWordValue.english else currentWordValue.russian
        _correctAnswer.value = correctAnswerValue

        if (_selectedOption.value == correctAnswerValue) {
            val currentScore = _score.value ?: 0
            val consecutiveCorrect = (_consecutiveCorrectAnswers.value ?: 0) + 1
            _consecutiveCorrectAnswers.value = consecutiveCorrect
            val additionalScore = if (consecutiveCorrect >= 2) 0.2 * consecutiveCorrect else 0.0
            _score.value = currentScore + 1 + additionalScore.toInt()
        } else {
            _consecutiveCorrectAnswers.value = 0
        }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
package ru.madfinal.launguageapp.presentation.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.madfinal.launguageapp.domain.auth.usecase.SignupUseCase
import ru.madfinal.launguageapp.presentation.common.UiState

class SignupViewModel(
    private val signupUseCase: SignupUseCase
) : ViewModel() {
    private val _signupState = MutableLiveData<UiState>()
    val signupState: LiveData<UiState> = _signupState

    private val disposables = CompositeDisposable()

    fun signUp(email: String, name: String, lastName: String, password: String) {
        val validationResult = signupUseCase.validateCredentials(email, password)

        if (!validationResult.isValid) {
            _signupState.value = UiState.Error(validationResult.errorMessage)
            return
        }

        _signupState.value = UiState.Loading

        val disposable = signupUseCase.execute(email, name, lastName, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user -> _signupState.value = UiState.Success(user) },
                { error ->
                    _signupState.value = UiState.Error(error.message ?: "Ошибка регистрации")
                }
            )

        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
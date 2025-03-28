package ru.madfinal.launguageapp.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.madfinal.launguageapp.domain.auth.usecase.LoginUseCase
import ru.madfinal.launguageapp.presentation.common.UiState

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _loginState = MutableLiveData<UiState>()
    val loginState: LiveData<UiState> = _loginState

    private val disposables = CompositeDisposable()

    fun login(email: String, password: String) {
        val validationResult = loginUseCase.validateCredentials(email, password)

        if (!validationResult.isValid) {
            _loginState.value = UiState.Error(validationResult.errorMessage)
            return
        }

        _loginState.value = UiState.Loading

        val disposable = loginUseCase.execute(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user -> _loginState.value = UiState.Success(user) },
                { error ->
                    _loginState.value = UiState.Error(error.message ?: "Ошибка авторизации")
                }
            )

        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
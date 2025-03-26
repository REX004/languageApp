package ru.madfinal.launguageapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.madfinal.launguageapp.domain.main.MainRepository
import ru.madfinal.launguageapp.domain.models.User

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _topUsers = MutableLiveData<List<User>>()
    val topUsers: LiveData<List<User>> = _topUsers

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    private val compositeDisposable = CompositeDisposable()

    fun loadTopUsers() {
        _loading.value = true
        compositeDisposable.add(
            repository.getTopUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ users ->
                    _topUsers.value = users
                    _loading.value = false
                }, { throwable ->
                    _error.value = throwable.message ?: "Unknown error"
                    _loading.value = false
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
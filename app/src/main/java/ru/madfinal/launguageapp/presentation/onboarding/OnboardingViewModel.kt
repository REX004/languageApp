package ru.madfinal.launguageapp.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.madfinal.launguageapp.domain.entities.OnboardingItem
import ru.madfinal.launguageapp.domain.usecases.GetNextOnboardingItemUseCase
import ru.madfinal.launguageapp.domain.usecases.IsQueueEmptyUseCase
import ru.madfinal.launguageapp.domain.usecases.MarkOnboardingCompleteUseCase
import ru.madfinal.launguageapp.presentation.onboarding.model.OnboardingUiState

class OnboardingViewModel : ViewModel(), KoinComponent {
    private val getNextOnboardingItemUseCase: GetNextOnboardingItemUseCase by inject()
    private val isQueueEmptyUseCase: IsQueueEmptyUseCase by inject()
    private val markOnboardingCompleteUseCase: MarkOnboardingCompleteUseCase by inject()

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    private var onboardingQueue: List<OnboardingItem> = emptyList()

    init {
        loadOnboardingItems()
        loadNextItem()
        observeQueueEmptyState()
    }

    private fun loadOnboardingItems() {
        val repository: ru.madfinal.launguageapp.domain.repositories.OnboardingRepository by inject()
        onboardingQueue = repository.initializeQueue()
    }

    fun loadNextItem() {
        if (_uiState.value.currentIndex < onboardingQueue.size) {
            _uiState.value = _uiState.value.copy(
                currentItem = onboardingQueue[_uiState.value.currentIndex],
                currentIndex = _uiState.value.currentIndex + 1
            )
        } else {
            _uiState.value = _uiState.value.copy(currentItem = null)
        }
    }

    private fun observeQueueEmptyState() {
        viewModelScope.launch {
            isQueueEmptyUseCase().collectLatest { isEmpty ->
                _uiState.value = _uiState.value.copy(isQueueEmpty = isEmpty)
            }
        }
    }

    fun markOnboardingCompleteAndNavigate() {
        viewModelScope.launch {
            markOnboardingCompleteUseCase()
            // Trigger navigation (e.g., using a SharedFlow or callback)
        }
    }

    fun handleSignInClick() {
        if (_uiState.value.isQueueEmpty) {
            // Trigger navigation to Placeholder screen
        }
    }

    fun getCurrentIndex(): Int {
        return _uiState.value.currentIndex - 1
    }

    fun getQueueSize(): Int {
        return onboardingQueue.size
    }
}
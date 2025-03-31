package ru.madfinal.launguageapp.presentation.onboarding.model

data class OnboardingUiState(
    val currentItem: ru.madfinal.launguageapp.domain.entities.OnboardingItem? = null,
    val isQueueEmpty: Boolean = false,
    val currentIndex: Int = 0
)
package ru.madfinal.launguageapp.domain.usecases

import ru.madfinal.launguageapp.domain.repositories.OnboardingRepository

class MarkOnboardingCompleteUseCase(private val repository: OnboardingRepository) {
    suspend operator fun invoke() {
        repository.markOnboardingComplete()
    }
}
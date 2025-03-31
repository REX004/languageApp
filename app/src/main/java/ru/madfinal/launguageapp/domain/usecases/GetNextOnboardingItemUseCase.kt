package ru.madfinal.launguageapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.madfinal.launguageapp.domain.entities.OnboardingItem
import ru.madfinal.launguageapp.domain.repositories.OnboardingRepository

class GetNextOnboardingItemUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Flow<OnboardingItem?> {
        return repository.getNextItem()
    }
}
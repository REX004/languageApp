package ru.madfinal.launguageapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.madfinal.launguageapp.domain.repositories.OnboardingRepository

class IsQueueEmptyUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Flow<Boolean> {
        return repository.isQueueEmpty()
    }
}
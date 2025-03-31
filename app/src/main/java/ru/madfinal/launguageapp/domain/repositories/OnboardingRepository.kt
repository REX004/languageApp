package ru.madfinal.launguageapp.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.madfinal.launguageapp.domain.entities.OnboardingItem

interface OnboardingRepository {
    fun getNextItem(): Flow<OnboardingItem?>
    fun isQueueEmpty(): Flow<Boolean>
    suspend fun markOnboardingComplete()
    suspend fun isOnboardingComplete(): Boolean
    fun initializeQueue(): List<OnboardingItem>
}
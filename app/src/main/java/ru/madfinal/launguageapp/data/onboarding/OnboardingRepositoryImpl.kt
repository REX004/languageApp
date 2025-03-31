package ru.madfinal.launguageapp.data.onboarding

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.domain.entities.OnboardingItem
import ru.madfinal.launguageapp.domain.repositories.OnboardingRepository

private val Context.dataStore by preferencesDataStore(name = "onboarding_prefs")
private val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")

class OnboardingRepositoryImpl(private val context: Context, private val localDataSource: LocalOnboardingDataSource) : OnboardingRepository {

    private val _currentItem = MutableStateFlow<OnboardingItem?>(null)
    override fun getNextItem(): Flow<OnboardingItem?> = _currentItem

    private val _isQueueEmpty = MutableStateFlow(false)
    override fun isQueueEmpty(): Flow<Boolean> = _isQueueEmpty

    private val onboardingQueue = localDataSource.getOnboardingQueue().toMutableList()
    private var currentItemIndex = 0

    init {
        loadNextItem()
    }

    private fun loadNextItem() {
        if (currentItemIndex < onboardingQueue.size) {
            _currentItem.value = onboardingQueue[currentItemIndex++]
            _isQueueEmpty.value = currentItemIndex >= onboardingQueue.size
        } else {
            _currentItem.value = null
            _isQueueEmpty.value = true
        }
    }

    override suspend fun markOnboardingComplete() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETE] = true
        }
    }

    override suspend fun isOnboardingComplete(): Boolean {
        return context.dataStore.data.first()[ONBOARDING_COMPLETE] ?: false
    }

    override fun initializeQueue(): List<OnboardingItem> {
        return localDataSource.getOnboardingQueue()
    }
}
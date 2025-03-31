package ru.madfinal.launguageapp.data.onboarding

import android.content.Context
import org.koin.core.component.KoinComponent
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.domain.entities.OnboardingItem

class LocaleOnboardingDataSource(private val context: Context) : KoinComponent {
    fun getOnboardingQueue(): List<OnboardingItem> {
        return listOf(
            OnboardingItem(
                R.drawable.status_onboard1,
                context.getString(R.string.onboarding1_description)
            ),
            OnboardingItem(
                R.drawable.status_onboard2,
                context.getString(R.string.onboarding2_des)
            ),
            OnboardingItem(R.drawable.status_onboard3, context.getString(R.string.onboarding_3_des))
        )
    }

}
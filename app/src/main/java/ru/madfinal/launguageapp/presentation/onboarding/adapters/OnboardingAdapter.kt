package ru.madfinal.launguageapp.presentation.onboarding.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.madfinal.launguageapp.presentation.onboarding.Onboarding1Fragment
import ru.madfinal.launguageapp.presentation.onboarding.Onboarding2Fragment
import ru.madfinal.launguageapp.presentation.onboarding.Onboarding3Fragment

class OnboardingAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Onboarding1Fragment()
            1 -> Onboarding2Fragment()
            2 -> Onboarding3Fragment()
            else -> {
                throw RuntimeException("Invalid position")
            }
        }
    }
}
package ru.madfinal.launguageapp.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.databinding.FragmentOnboardingMainBinding
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment
import ru.madfinal.launguageapp.presentation.onboarding.adapters.OnboardingAdapter
import ru.madfinal.launguageapp.presentation.util.navigateWithAnimation

class OnboardingMainFragment :
    BaseFragment<FragmentOnboardingMainBinding>(FragmentOnboardingMainBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAdapter()
        setupObserves()
    }

    private val imageResources =
        intArrayOf(
            R.drawable.status_onboard1,
            R.drawable.status_onboard2,
            R.drawable.status_onboard3
        )

    private val adapter: OnboardingAdapter by lazy { OnboardingAdapter(requireFragmentManager()) }

    private fun setupObserves() {
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                changeButton()
                changeStatus()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun changeButton() {

        binding.apply {
            nextBt.text = when (viewPager.currentItem) {
                0 -> resources.getString(R.string.next)
                1 -> resources.getString(R.string.more)
                2 -> resources.getString(R.string.choose_a_language)
                else -> {
                    resources.getString(R.string.more)
                }
            }
        }
    }

    override fun applyClick() {
        super.applyClick()
        binding.apply {
            nextBt.setOnClickListener {
                if (viewPager.currentItem == 2) {
                    findNavController().navigateWithAnimation(R.id.languageSelectFragment)
                } else {
                    viewPager.currentItem += 1
                }
            }

            skipBt.setOnClickListener {
                findNavController().navigateWithAnimation(R.id.languageSelectFragment)
            }
        }
    }

    private fun changeStatus() {
        binding.apply {
            statusBar.setImageResource(imageResources[binding.viewPager.currentItem])
        }
    }

    private fun observeAdapter() {
        binding.viewPager.adapter = adapter
    }

}
package ru.madfinal.launguageapp.presentation.main.selector

import android.os.Bundle
import android.view.View
import com.google.android.material.card.MaterialCardView
import ru.madfinal.launguageapp.data.datasource.locale.UserPreferences
import ru.madfinal.launguageapp.databinding.FragmentLanguageSelectBinding
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment

class LanguageSelectFragment :
    BaseFragment<FragmentLanguageSelectBinding>(FragmentLanguageSelectBinding::inflate) {

    private lateinit var languageViews: Map<AppLanguage, MaterialCardView>
    private lateinit var selectedLanguage: AppLanguage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = UserPreferences(requireContext())
        selectedLanguage = prefs.getSelectedLanguage()

        languageViews = mapOf(
            AppLanguage.RUSSIAN to binding.russian,
            AppLanguage.ENGLISH to binding.english,
            AppLanguage.CHINESE to binding.chinese,
            AppLanguage.BELARUS to binding.belarus,
            AppLanguage.KAZAKH to binding.kazakh
        )

        setupLanguageSelection()
    }

    override fun applyClick() {
        super.applyClick()

        binding.choseBt.setOnClickListener {
            backPressed()
        }
    }

    private fun setupLanguageSelection() {
        languageViews.forEach { (language, view) ->
            view.setOnClickListener {
                updateSelection(language)
            }
        }

        updateSelection(selectedLanguage)
    }

    private fun updateSelection(language: AppLanguage) {
        selectedLanguage = language

        languageViews.forEach { (lang, view) ->
            view.isSelected = lang == language
        }

        UserPreferences(requireContext()).setSelectedLanguage(language)
    }
}
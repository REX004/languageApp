package ru.madfinal.launguageapp.presentation.exercise.animals.states

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.databinding.FragmentAnimalsSuccessBinding
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment
import ru.madfinal.launguageapp.presentation.util.navigateWithAnimation

class AnimalsSuccessFragment :
    BaseFragment<FragmentAnimalsSuccessBinding>(FragmentAnimalsSuccessBinding::inflate) {

    private val args: AnimalsSuccessFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
//        binding.scoreText.text = "Score: ${args.score}"
//        binding.streakText.text = "Streak: ${args.consecutiveCorrect}"
    }

    private fun setupListeners() {
        binding.nextBt.setOnClickListener {
            // Возвращаемся на экран с животными и загружаем новое животное
            findNavController().navigateWithAnimation(R.id.animalsFragment)
        }

        binding.backBt.setOnClickListener {
            // Возвращаемся на главный экран
            findNavController().navigateWithAnimation(R.id.mainScreenFragment)
        }
    }
}
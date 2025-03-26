package ru.madfinal.launguageapp.presentation.exercise.animals

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.databinding.FragmentAnimalsBinding
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment

class AnimalsFragment : BaseFragment<FragmentAnimalsBinding>(FragmentAnimalsBinding::inflate) {

    private val viewModel: AnimalsViewModel by viewModel()
    private var currentBitmap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.currentAnimal.observe(viewLifecycleOwner) { animal ->
            loadAnimalImage(animal.image)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.checkBt.isEnabled = !isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        }

        viewModel.isAnswerCorrect.observe(viewLifecycleOwner) { isCorrect ->
            if (isCorrect) {
                // Переходим на экран успеха
                val action =
                    AnimalsFragmentDirections.actionAnimalsFragmentToAnimalsSuccessFragment(
                        score = viewModel.score.value ?: 0,
                        consecutiveCorrect = viewModel.consecutiveCorrectAnswers.value ?: 0
                    )
                findNavController().navigate(action)
            } else {
                // Переходим на экран ошибки
                val action = AnimalsFragmentDirections.actionAnimalsFragmentToAnimalsErrorFragment(
                    correctWord = viewModel.recognizedAnimal.value ?: "unknown"
                )
                findNavController().navigate(action)
            }
        }
    }

    private fun setupListeners() {
        binding.checkBt.setOnClickListener {
            val userAnswer = binding.writerEt.text.toString().trim()
            if (userAnswer.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter an answer", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            currentBitmap?.let { bitmap ->
                viewModel.checkAnswer(userAnswer, bitmap)
            } ?: run {
                Toast.makeText(requireContext(), "Image not loaded yet", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backBt.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadAnimalImage(imageUrl: String) {
        binding.animalImage.setImageDrawable(null)
        currentBitmap = null

        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .listener(object : RequestListener<Bitmap> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT)
                        .show()
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    model: Any,
                    target: Target<Bitmap>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.let {
                        currentBitmap = it
                    }
                    return false
                }
            })
            .into(binding.animalImage)
    }
}
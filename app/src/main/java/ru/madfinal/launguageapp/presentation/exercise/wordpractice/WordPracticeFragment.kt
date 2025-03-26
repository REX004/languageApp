package ru.madfinal.launguageapp.presentation.exercise.wordpractice

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.databinding.FragmentWordPracticeBinding
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment
import ru.madfinal.launguageapp.presentation.exercise.wordpractice.adapter.WordAdapter

class WordPracticeFragment :
    BaseFragment<FragmentWordPracticeBinding>(FragmentWordPracticeBinding::inflate) {

    private val viewModel: WordPracticeViewModel by viewModel()
    private val adapter: WordAdapter by lazy {
        WordAdapter(
            onItemClick = { selectedOption ->
                viewModel.onOptionSelected(selectedOption)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wordsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.wordsRv.adapter = adapter

        observeViewModel()

        binding.nextBt.setOnClickListener {
            viewModel.loadNextWord()
            binding.checkBt.visibility = View.VISIBLE
            binding.nextBt.visibility = View.GONE
        }

        binding.checkBt.setOnClickListener { // checkBt!
            binding.checkBt.visibility = View.GONE
            binding.nextBt.visibility = View.VISIBLE
            if (viewModel.selectedOption.value != null) {
                viewModel.checkAnswer()
            } else {
                Toast.makeText(requireContext(), "Выберите вариант", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backBt.setOnClickListener {
            backPressed()
        }
    }

    private fun observeViewModel() {
        viewModel.currentWord.observe(viewLifecycleOwner) { word ->
            binding.wordTxt.text =
                if (viewModel.isEnglishTurn.value == true) word.english else word.russian
            binding.transcriptionTxt.text =
                if (viewModel.isEnglishTurn.value == true) word.transcription_english else ""
        }

        viewModel.options.observe(viewLifecycleOwner) { options ->
            val isEnglish = viewModel.isEnglishTurn.value ?: true

            adapter.submitList(options)
        }
        viewModel.score.observe(viewLifecycleOwner) { score ->
        }

        viewModel.selectedOption.observe(viewLifecycleOwner) { _ ->
            updateButtonColors()
        }
        viewModel.correctAnswer.observe(viewLifecycleOwner) {
            updateButtonColors()
        }
        viewModel.isAnswerChecked.observe(viewLifecycleOwner) { isChecked ->
            updateButtonColors()
            binding.nextBt.visibility = View.VISIBLE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun updateButtonColors() {
        val selected = viewModel.selectedOption.value
        val correct = viewModel.correctAnswer.value
        val isChecked = viewModel.isAnswerChecked.value ?: false

        for (i in 0 until binding.wordsRv.childCount) {
            val holder =
                binding.wordsRv.findViewHolderForAdapterPosition(i) as? WordAdapter.ViewHolder
            holder?.let {
                val button = it.itemView.findViewById<LinearLayout>(R.id.container)
                val optionText = viewModel.options.value?.get(i)

                if (isChecked) {
                    if (optionText == correct) {
                        button.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    } else if (optionText == selected) {
                        button.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.orange
                            )
                        )
                    } else {
                        button.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_light
                            )
                        )
                    }
                } else {
                    button.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_light
                        )
                    )
                }
            }
        }
    }
}
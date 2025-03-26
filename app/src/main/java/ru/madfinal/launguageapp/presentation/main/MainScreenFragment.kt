package ru.madfinal.launguageapp.presentation.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.databinding.FragmentMainScreenBinding
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment
import ru.madfinal.launguageapp.presentation.main.adapter.TopUsersAdapter
import ru.madfinal.launguageapp.presentation.util.navigateWithAnimation

class MainScreenFragment :
    BaseFragment<FragmentMainScreenBinding>(FragmentMainScreenBinding::inflate) {

    private val viewModel: MainViewModel by viewModel()
    private val adapter: TopUsersAdapter by lazy { TopUsersAdapter(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        observeAdapter()
        viewModel.loadTopUsers() // Загружаем данные при создании фрагмента
    }

    override fun applyClick() {
        super.applyClick()
        binding.apply {
            wordPracticeBt.setOnClickListener {
                findNavController().navigateWithAnimation(R.id.wordPracticeFragment)
            }
        }
        binding.animalBt.setOnClickListener {
            findNavController().navigateWithAnimation(R.id.animalsFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.topUsers.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            // Показываем/скрываем ProgressBar (если он есть в разметке)
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun observeAdapter() {
        binding.topUserRv.apply {
            adapter = this@MainScreenFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
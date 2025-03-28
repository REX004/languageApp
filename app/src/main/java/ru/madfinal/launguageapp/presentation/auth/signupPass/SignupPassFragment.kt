package ru.madfinal.launguageapp.presentation.auth.signupPass

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.databinding.FragmentSignupPassBinding
import ru.madfinal.launguageapp.presentation.auth.login.LoginViewModel
import ru.madfinal.launguageapp.presentation.common.UiState
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment


class SignupPassFragment :
    BaseFragment<FragmentSignupPassBinding>(FragmentSignupPassBinding::inflate) {
    private val viewModel: LoginViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun applyClick() {
        super.applyClick()
        binding.backBt.setOnClickListener {
            backPressed()
        }
        binding.loginBt.setOnClickListener {
            performLogin()
        }
        binding.loginBt.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
    }

    private fun performLogin() {
        val email = binding.passwordConfirmEt.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()

        viewModel.login(email, password)
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.loginBt.isEnabled = false
                }

                is UiState.Error -> {
                    binding.loginBt.isEnabled = true
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success<*> -> {
                    binding.loginBt.isEnabled = true
                    findNavController().navigate(R.id.mainScreenFragment)
                }
            }
        }
    }

}
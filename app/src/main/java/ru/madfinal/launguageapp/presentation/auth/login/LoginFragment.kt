package ru.madfinal.launguageapp.presentation.auth.login


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import ru.madfinal.launguageapp.databinding.FragmentLoginBinding
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.data.datasource.locale.UserPreferences
import ru.madfinal.launguageapp.presentation.common.UiState


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = UserPreferences(requireContext())
        if (prefs.isLoggedIn()) {
            findNavController().navigate(R.id.mainScreenFragment)
            return
        }
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
    }

    private fun performLogin() {
        val email = binding.emailEt.text.toString().trim()
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
                    val prefs = UserPreferences(requireContext())
                    prefs.setLoggedIn(true)
                    findNavController().navigate(R.id.mainScreenFragment)
                }
            }
        }
    }

}
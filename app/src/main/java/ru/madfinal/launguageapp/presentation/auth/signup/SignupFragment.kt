package ru.madfinal.launguageapp.presentation.auth.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.databinding.FragmentSignupBinding
import ru.madfinal.launguageapp.presentation.common.UiState
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment
import java.io.File

class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    private val viewModel: SignupViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyClick()
        observeViewModel()
    }

    override fun applyClick() {
        super.applyClick()

        binding.backBt.setOnClickListener {
            backPressed()
        }

        binding.toolbarContainer.setOnClickListener {
            showPdf()
        }

        binding.loginBt.setOnClickListener {
            performSignUp()
        }
    }

    private fun performSignUp() {
        val email = binding.emailEt.text.toString().trim()
        val name = binding.nameEt.text.toString().trim()
        val lastName = binding.lastNameEt.text.toString().trim()
        val password = "1234Aa1 !"

        viewModel.signUp(email, name, lastName, password)
    }

    private fun observeViewModel() {
        viewModel.signupState.observe(viewLifecycleOwner) { state ->
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

    private fun showPdf() {
        binding.pdfChecker.initWithFile(
            file = filePath()
        )
    }

    private fun filePath(): File {
        val tempFile = File(requireContext().cacheDir, "terms.pdf").apply {
            outputStream().use { output ->
                requireContext().assets.open("pdf/").use { it.copyTo(output) }
            }
        }
        return tempFile
    }
}
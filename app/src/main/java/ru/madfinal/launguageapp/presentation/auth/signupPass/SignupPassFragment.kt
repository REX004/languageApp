package ru.madfinal.launguageapp.presentation.auth.signupPass

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.data.datasource.locale.UserPreferences
import ru.madfinal.launguageapp.databinding.FragmentSignupPassBinding
import ru.madfinal.launguageapp.domain.models.User
import ru.madfinal.launguageapp.presentation.auth.login.LoginViewModel
import ru.madfinal.launguageapp.presentation.common.UiState
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment
import java.io.File


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
        binding.accessTxt.setOnClickListener {
            openPdfFile("pdf/terms.pdf")
        }
    }


    private fun openPdfFile(assetPath: String) {
        try {
            // Копируем файл из assets во временный файл
            val tempFile = File(requireContext().cacheDir, "temp.pdf").apply {
                outputStream().use { output ->
                    requireContext().assets.open(assetPath).use { it.copyTo(output) }
                }
            }

            // Получаем URI и запускаем intent с выбором приложения
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                tempFile
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            startActivity(Intent.createChooser(intent, "Открыть PDF в"))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
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
                    val prefs = UserPreferences(requireContext())

                    val user = state.data as? User
                    user?.let {
                        prefs.saveUserId(it.id)
                        prefs.setLoggedIn(true)
                    }
                    findNavController().navigate(R.id.mainScreenFragment)
                }
            }
        }
    }

}
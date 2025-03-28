package ru.madfinal.launguageapp.presentation.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.fragment.findNavController;
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.databinding.FragmentProfileBinding;
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment;
import ru.madfinal.launguageapp.presentation.util.navigateWithAnimation
import java.io.File

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private lateinit var sharedPreferences: SharedPreferences;
    private var waitingForCroppedImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Устанавливаем обработчик для получения обрезанного изображения
        setupCroppedImageObserver()

        // Загружаем сохраненное изображение только если не ожидаем обрезанное
        if (!waitingForCroppedImage) {
            loadSavedProfileImage()
        }

        // Устанавливаем текст кнопки из SharedPreferences
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", true)
        val buttonText = sharedPreferences.getString(
            "theme_button_text",
            if (isDarkMode) "Switch to Light" else "Switch to Dark"
        )
        binding.switchModeBt.text = buttonText

        binding.changeImageBt.setOnClickListener {
            waitingForCroppedImage = true
            pickImageLauncher.launch("image/*")
        }

        binding.switchModeBt.setOnClickListener {
            toggleTheme()
        }

        binding.logoutBt.setOnClickListener {
            logout()
        }
        binding.changeLanguageBt.setOnClickListener {
            findNavController().navigateWithAnimation(R.id.languageSelectFragment)
        }
    }

    private fun loadSavedProfileImage() {
        val savedImageUri = sharedPreferences.getString("profile_image_uri", null)
        Log.d("ProfileFragment", "Loading saved image: $savedImageUri")
        if (savedImageUri != null) {
            try {
                binding.profileImage.setImageURI(Uri.parse(savedImageUri))
            } catch (e: Exception) {
                Log.e("ProfileFragment", "Error loading saved image: ${e.message}", e)
            }
        }
    }

    private fun setupCroppedImageObserver() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("croppedImage")
            ?.observe(viewLifecycleOwner) { uriString ->
                Log.d("ProfileFragment", "Received cropped image URI: $uriString")

                if (!uriString.isNullOrEmpty()) {
                    try {
                        val uri = Uri.parse(uriString)

                        // Проверяем существование файла
                        val file = uri.path?.let { File(it) }
                        Log.d(
                            "ProfileFragment",
                            "Cropped file exists: ${file?.exists()}, path: ${file?.absolutePath}"
                        )

                        // Устанавливаем изображение
                        binding.profileImage.setImageURI(null) // Сбрасываем текущее изображение
                        binding.profileImage.setImageURI(uri)  // Устанавливаем новое

                        // Сохраняем URI в SharedPreferences
                        sharedPreferences.edit().putString("profile_image_uri", uriString).apply()
                        Log.d("ProfileFragment", "Saved cropped image URI to preferences")

                        // Сбрасываем флаг ожидания
                        waitingForCroppedImage = false

                        // Очищаем savedStateHandle
                        findNavController().currentBackStackEntry?.savedStateHandle?.remove("croppedImage")
                    } catch (e: Exception) {
                        Log.e("ProfileFragment", "Error processing cropped image: ${e.message}", e)
                        waitingForCroppedImage = false
                        loadSavedProfileImage() // Загружаем сохраненное изображение в случае ошибки
                    }
                } else {
                    Log.w("ProfileFragment", "Received empty cropped image URI")
                    waitingForCroppedImage = false
                    loadSavedProfileImage()
                }
            }
    }

    private fun toggleTheme() {
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", true)
        val newIsDarkMode = !isDarkMode
        val newMode =
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES

        // Сохраняем новое значение и текст кнопки
        sharedPreferences.edit()
            .putBoolean("dark_mode", newIsDarkMode)
            .putString(
                "theme_button_text",
                if (newIsDarkMode) "Switch to Light" else "Switch to Dark"
            )
            .apply()

        // Применяем новый режим (это пересоздаст активность)
        AppCompatDelegate.setDefaultNightMode(newMode)
    }

    private fun logout() {
        sharedPreferences.edit().clear().apply()

        val userPrefs = requireContext().getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        userPrefs.edit().clear().apply()

        findNavController().navigateWithAnimation(R.id.loginFragment)
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // НЕ сохраняем оригинальное изображение в SharedPreferences
                // Просто переходим на экран обрезки
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToResizePhotoFragment(it.toString());
                findNavController().navigate(action);
            }
        };
}
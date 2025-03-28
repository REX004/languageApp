package ru.madfinal.launguageapp.domain.auth.usecase

import android.util.Patterns
import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.domain.auth.repository.AuthRepository
import ru.madfinal.launguageapp.domain.models.User
import ru.madfinal.launguageapp.domain.models.ValidationResult

class LoginUseCase(private val authRepository: AuthRepository) {

    fun execute(email: String, password: String): Single<User> {
        val validationResult = validateCredentials(email, password)

        return if (validationResult.isValid) {
            authRepository.login(email, password)
        } else {
            Single.error(IllegalArgumentException(validationResult.errorMessage))
        }
    }

    fun validateCredentials(email: String, password: String): ValidationResult {
        when {
            !validateEmail(email) -> return ValidationResult(false, "Неверный формат email")
            !validatePassword(password) -> return ValidationResult(
                false,
                "Пароль должен содержать не менее 6 символов"
            )

            else -> return ValidationResult(true)
        }
    }

    private fun validateEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 6
    }
}
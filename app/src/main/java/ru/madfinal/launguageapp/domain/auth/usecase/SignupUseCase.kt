package ru.madfinal.launguageapp.domain.auth.usecase

import android.util.Patterns
import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.domain.auth.repository.AuthRepository
import ru.madfinal.launguageapp.domain.models.User
import ru.madfinal.launguageapp.domain.models.ValidationResult
import java.util.regex.Pattern

class SignupUseCase(private val authRepository: AuthRepository) {

    fun execute(email: String, name: String, lastName: String, password: String): Single<User> {
        val validationResult = validateCredentials(email, password)

        return if (validationResult.isValid) {
            authRepository.signUp(email, password, name, lastName)
        } else {
            Single.error(IllegalArgumentException(validationResult.errorMessage))
        }
    }

    fun validateCredentials(email: String, password: String): ValidationResult {
        return when {
            !validateEmail(email) -> ValidationResult(false, "Некорректный email")
            !validatePassword(password) -> ValidationResult(
                false,
                "Пароль должен содержать минимум 8 символов, заглавную и строчную букву, цифру, пробел и спецсимвол"
            )
            else -> ValidationResult(true)
        }
    }

    private fun validateEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return with(password) {
            length >= 8 &&
                    any { it.isUpperCase() } &&
                    any { it.isLowerCase() } &&
                    any { it.isDigit() } &&
                    any { it == ' ' } &&
                    any { !it.isLetterOrDigit() && it != ' ' }
        }
    }
}
package ru.madfinal.launguageapp.domain.auth.usecase

import android.util.Patterns
import ru.madfinal.launguageapp.data.datasource.network.supabase.ResponseState

class EmailValidationUseCase {
    fun execute(email: String): ResponseState<Unit> {
        return when {
            isEmailInvalid(email) -> ResponseState.Error("Неверный формат email")
            else -> ResponseState.Success(Unit)
        }
    }

    private fun isEmailInvalid(email: String): Boolean {
        return email.isNotEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
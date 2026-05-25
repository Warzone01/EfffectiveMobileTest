package com.kirdevelopment.feature.auth.domain.usecase

import com.kirdevelopment.domain.model.validation.ValidationResult
import com.kirdevelopment.domain.usecase.ValidateEmailUseCase
import com.kirdevelopment.domain.usecase.ValidatePasswordUseCase
import javax.inject.Inject

/**
 * Use case для валидации формы входа.
 * Возвращает список ValidationResult — пустой список означает, что всё валидно.
 */
class ValidateLoginFormUseCase @Inject constructor(
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase
) {
    operator fun invoke(email: String, password: String): LoginValidationResult {
        val emailResult = validateEmail.execute(email)
        val passwordResult = validatePassword.execute(password)

        return LoginValidationResult(
            emailValidation = emailResult,
            passwordValidation = passwordResult,
            isValid = emailResult.isValid && passwordResult.isValid
        )
    }
}

/**
 * Агрегированный результат валидации формы входа.
 */
data class LoginValidationResult(
    val emailValidation: ValidationResult,
    val passwordValidation: ValidationResult,
    val isValid: Boolean
)

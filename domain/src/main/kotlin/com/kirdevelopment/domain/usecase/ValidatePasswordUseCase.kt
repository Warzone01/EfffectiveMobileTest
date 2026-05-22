package com.kirdevelopment.domain.usecase

import com.kirdevelopment.domain.model.validation.ValidationResult
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    fun execute(password: String): ValidationResult {
        return if (password.isNotBlank()) {
            ValidationResult(isValid = true)
        } else {
            ValidationResult(isValid = false, reason = "Пароль пустой")
        }
    }
}

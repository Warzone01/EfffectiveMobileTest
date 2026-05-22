package com.kirdevelopment.domain.usecase

import com.kirdevelopment.domain.model.validation.ValidationResult
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val cyrillicRegex = Regex("[А-Яа-яЁё]")

    fun execute(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(isValid = false, reason = "Email пустой")
        }
        if (cyrillicRegex.containsMatchIn(email)) {
            return ValidationResult(isValid = false, reason = "Кириллица запрещена")
        }
        if (!emailRegex.matches(email)) {
            return ValidationResult(isValid = false, reason = "Неверный формат email")
        }
        return ValidationResult(isValid = true)
    }
}

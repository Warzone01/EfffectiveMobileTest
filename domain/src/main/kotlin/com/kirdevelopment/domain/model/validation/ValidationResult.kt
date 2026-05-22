package com.kirdevelopment.domain.model.validation

/**
 * Результат валидации полей в domain слое.
 * Не зависит от Android/UI и может использоваться в любых presentation слоях.
 */
data class ValidationResult(
    val isValid: Boolean,
    val reason: String? = null
)

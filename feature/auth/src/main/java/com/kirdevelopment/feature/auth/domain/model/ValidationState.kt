package com.kirdevelopment.feature.auth.domain.model

/**
 * Состояние валидации отдельного поля формы входа.
 * Хранит текущее значение, флаг ошибки и текст ошибки (UiText для независимости от Android).
 */
data class ValidationState(
    val value: String = "",
    val isError: Boolean = false,
    val errorMessage: String? = null
) {
    companion object {
        fun valid(value: String = "") = ValidationState(value = value, isError = false, errorMessage = null)
        fun invalid(value: String = "", errorMessage: String) =
            ValidationState(value = value, isError = true, errorMessage = errorMessage)
    }
}

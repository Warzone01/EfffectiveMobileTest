package com.kirdevelopment.feature.auth.presentation.login

/**
 * События от UI-слоя (пользовательские действия).
 * Обрабатываются ViewModel для обновления состояния.
 */
sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object LoginClicked : LoginUiEvent
}

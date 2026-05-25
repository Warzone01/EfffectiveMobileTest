package com.kirdevelopment.feature.auth.presentation.login

import com.kirdevelopment.feature.auth.domain.model.ValidationState
import com.kirdevelopment.feature.auth.presentation.model.LoginError

/**
 * Состояние экрана входа.
 * Immutable — ViewModel всегда эмитит новый экземпляр при изменении.
 */
data class LoginUiState(
    val email: ValidationState = ValidationState.valid(),
    val password: ValidationState = ValidationState.valid(),
    val isLoading: Boolean = false,
    val loginError: LoginError? = null
)

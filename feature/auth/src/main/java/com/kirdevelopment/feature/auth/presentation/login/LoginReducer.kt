package com.kirdevelopment.feature.auth.presentation.login

import com.kirdevelopment.feature.auth.domain.model.ValidationState
import com.kirdevelopment.feature.auth.domain.usecase.LoginValidationResult
import com.kirdevelopment.feature.auth.presentation.model.LoginError

/**
 * Редьюсер состояния экрана входа.
 * Чистая функция: принимает текущее состояние и событие, возвращает новое состояние.
 * Вся бизнес-логика обновления состояния сосредоточена здесь.
 */
class LoginReducer(
) {

    fun onEmailChanged(state: LoginUiState, email: String, validation: LoginValidationResult): LoginUiState {
        return state.copy(
            email = if (validation.emailValidation.isValid) {
                ValidationState.valid(email)
            } else {
                ValidationState.invalid(email, validation.emailValidation.reason.orEmpty())
            },
            loginError = null
        )
    }

    fun onPasswordChanged(state: LoginUiState, password: String, validation: LoginValidationResult): LoginUiState {
        return state.copy(
            password = if (validation.passwordValidation.isValid) {
                ValidationState.valid(password)
            } else {
                ValidationState.invalid(password, validation.passwordValidation.reason.orEmpty())
            },
            loginError = null
        )
    }

    fun onLoginValidation(state: LoginUiState, validation: LoginValidationResult): LoginUiState {
        return state.copy(
            email = if (validation.emailValidation.isValid) {
                ValidationState.valid(state.email.value)
            } else {
                ValidationState.invalid(state.email.value, validation.emailValidation.reason.orEmpty())
            },
            password = if (validation.passwordValidation.isValid) {
                ValidationState.valid(state.password.value)
            } else {
                ValidationState.invalid(state.password.value, validation.passwordValidation.reason.orEmpty())
            }
        )
    }

    fun onLoading(state: LoginUiState, isLoading: Boolean): LoginUiState {
        return state.copy(isLoading = isLoading, loginError = if (isLoading) null else state.loginError)
    }

    fun onLoginFailure(state: LoginUiState, loginError: LoginError): LoginUiState {
        return state.copy(
            isLoading = false,
            loginError = loginError
        )
    }
}

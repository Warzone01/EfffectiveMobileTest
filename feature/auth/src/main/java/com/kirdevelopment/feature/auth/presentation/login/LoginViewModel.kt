package com.kirdevelopment.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.feature.auth.domain.model.ValidationState
import com.kirdevelopment.feature.auth.domain.usecase.LoginUseCase
import com.kirdevelopment.feature.auth.domain.usecase.LoginValidationResult
import com.kirdevelopment.feature.auth.domain.usecase.ValidateLoginFormUseCase
import com.kirdevelopment.feature.auth.presentation.model.LoginError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateLoginForm: ValidateLoginFormUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<LoginUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    /**
     * Обработка событий от UI.
     * Каждый event приводит к обновлению состояния или эффекту.
     */
    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> onEmailChanged(event.email)
            is LoginUiEvent.PasswordChanged -> onPasswordChanged(event.password)
            LoginUiEvent.LoginClicked -> onLoginClicked()
        }
    }

    private fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(
            email = ValidationState.valid(email),
            loginError = null
        )
    }

    private fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            password = ValidationState.valid(password),
            loginError = null
        )
    }

    private fun onLoginClicked() {
        val currentState = _uiState.value
        val validation: LoginValidationResult = validateLoginForm(
            email = currentState.email.value,
            password = currentState.password.value
        )

        if (!validation.isValid) {
            val emailState = if (validation.emailValidation.isValid) {
                currentState.email
            } else {
                ValidationState.invalid(
                    value = currentState.email.value,
                    errorMessage = validation.emailValidation.reason.orEmpty()
                )
            }
            val passwordState = if (validation.passwordValidation.isValid) {
                currentState.password
            } else {
                ValidationState.invalid(
                    value = currentState.password.value,
                    errorMessage = validation.passwordValidation.reason.orEmpty()
                )
            }
            _uiState.value = currentState.copy(
                email = emailState,
                password = passwordState
            )
            return
        }

        performLogin()
    }

    private fun performLogin() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, loginError = null)

            val result = loginUseCase(
                email = _uiState.value.email.value,
                password = _uiState.value.password.value
            )

            _uiState.value = _uiState.value.copy(isLoading = false)

            when (result) {
                is AppResult.Success -> {
                    _uiEffect.send(LoginUiEffect.NavigateToMain)
                }

                is AppResult.Error -> {
                    val error = result.error
                    val errorMessage = when (error) {
                        is com.kirdevelopment.core.common.error.AppError.Network -> error.message
                        is com.kirdevelopment.core.common.error.AppError.Server -> error.message
                        is com.kirdevelopment.core.common.error.AppError.Validation -> error.message
                        is com.kirdevelopment.core.common.error.AppError.Database -> error.message
                        is com.kirdevelopment.core.common.error.AppError.Unknown -> error.throwable?.message
                    }

                    val loginError = when (error) {
                        is com.kirdevelopment.core.common.error.AppError.Network ->
                            LoginError.Network(error.code, error.message)

                        is com.kirdevelopment.core.common.error.AppError.Server ->
                            LoginError.Server(error.code, error.message)

                        is com.kirdevelopment.core.common.error.AppError.Validation ->
                            LoginError.Validation(error.field, error.message)

                        is com.kirdevelopment.core.common.error.AppError.Database ->
                            LoginError.Database(error.message)

                        is com.kirdevelopment.core.common.error.AppError.Unknown ->
                            LoginError.Unknown(error.throwable)
                    }

                    _uiState.value = _uiState.value.copy(loginError = loginError)
                    _uiEffect.send(
                        LoginUiEffect.ShowError(
                            errorMessage ?: "Неизвестная ошибка"
                        )
                    )
                }
            }
        }
    }
}

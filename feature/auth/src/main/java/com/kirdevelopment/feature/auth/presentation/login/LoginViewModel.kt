package com.kirdevelopment.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirdevelopment.core.common.error.AppError
import com.kirdevelopment.core.common.result.AppResult
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

    private companion object {
        const val VK_URL = "https://vk.com"
        const val OK_URL = "https://ok.ru"
    }

    private val reducer = LoginReducer()

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
            LoginUiEvent.VkClicked -> openVk()
            LoginUiEvent.OkClicked -> openOk()
        }
    }

    private fun openVk() {
        viewModelScope.launch {
            _uiEffect.send(LoginUiEffect.OpenBrowser(VK_URL))
        }
    }

    private fun openOk() {
        viewModelScope.launch {
            _uiEffect.send(LoginUiEffect.OpenBrowser(OK_URL))
        }
    }

    private fun onEmailChanged(email: String) {
        val currentState = _uiState.value
        val validation = validateLoginForm(email = email, password = currentState.password.value)
        reduce { reducer.onEmailChanged(it, email, validation) }
    }

    private fun onPasswordChanged(password: String) {
        val currentState = _uiState.value
        val validation = validateLoginForm(email = currentState.email.value, password = password)
        reduce { reducer.onPasswordChanged(it, password, validation) }
    }

    private fun onLoginClicked() {
        val currentState = _uiState.value
        val validation: LoginValidationResult = validateLoginForm(
            email = currentState.email.value,
            password = currentState.password.value
        )

        reduce { reducer.onLoginValidation(it, validation) }

        if (!validation.isValid) {
            return
        }

        performLogin()
    }

    private fun performLogin() {
        viewModelScope.launch {
            reduce { reducer.onLoading(it, isLoading = true) }

            val result = loginUseCase(
                email = _uiState.value.email.value,
                password = _uiState.value.password.value
            )

            reduce { reducer.onLoading(it, isLoading = false) }

            when (result) {
                is AppResult.Success -> {
                    _uiEffect.send(LoginUiEffect.NavigateToMain)
                }

                is AppResult.Error -> {
                    val error = result.error
                    val loginError = when (error) {
                        is AppError.Network ->
                            LoginError.Network(error.code, error.message)

                        is AppError.Server ->
                            LoginError.Server(error.code, error.message)

                        is AppError.Validation ->
                            LoginError.Validation(error.field, error.message)

                        is AppError.Database ->
                            LoginError.Database(error.message)

                        is AppError.Unknown ->
                            LoginError.Unknown(error.throwable)
                    }

                    reduce { reducer.onLoginFailure(it, loginError) }
                    _uiEffect.send(
                        LoginUiEffect.ShowError(
                            extractErrorMessage(error)
                        )
                    )
                }
            }
        }
    }

    private fun reduce(block: (LoginUiState) -> LoginUiState) {
        _uiState.value = block(_uiState.value)
    }

    private fun extractErrorMessage(error: AppError): String {
        return when (error) {
            is AppError.Network -> error.message ?: "Ошибка сети"
            is AppError.Server -> error.message ?: "Ошибка сервера"
            is AppError.Validation -> error.message ?: "Ошибка валидации"
            is AppError.Database -> error.message ?: "Ошибка базы данных"
            is AppError.Unknown -> error.throwable?.message ?: "Неизвестная ошибка"
        }
    }
}

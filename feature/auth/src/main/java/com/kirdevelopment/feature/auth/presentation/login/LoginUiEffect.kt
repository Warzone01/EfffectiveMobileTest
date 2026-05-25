package com.kirdevelopment.feature.auth.presentation.login

/**
 * Одноразовые эффекты от ViewModel к UI.
 * Потребляются один раз — навигация, тосты, диалоги.
 */
sealed interface LoginUiEffect {
    /** Перейти к главному экрану после успешного входа */
    data object NavigateToMain : LoginUiEffect

    /** Показать ошибку пользователю */
    data class ShowError(val message: String) : LoginUiEffect

    /** Открыть браузер по ссылке */
    data class OpenBrowser(val url: String) : LoginUiEffect
}

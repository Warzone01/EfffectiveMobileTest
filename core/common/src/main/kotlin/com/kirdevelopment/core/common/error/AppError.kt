package com.kirdevelopment.core.common.error

/**
 * Единая модель ошибок для всего приложения.
 * Позволяет централизованно маппить технические исключения в понятные UI-состояния.
 */
sealed interface AppError {
    data class Network(val code: Int? = null, val message: String? = null) : AppError
    data class Server(val code: Int? = null, val message: String? = null) : AppError
    data class Validation(val field: String? = null, val message: String? = null) : AppError
    data class Database(val message: String? = null) : AppError
    data class Unknown(val throwable: Throwable? = null) : AppError
}

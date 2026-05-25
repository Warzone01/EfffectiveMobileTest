package com.kirdevelopment.feature.auth.presentation.model

/**
 * Типизированные ошибки авторизации для UI.
 * Позволяют presentation слою реагировать на разные категории ошибок.
 */
sealed interface LoginError {
    data class Network(val code: Int?, val message: String?) : LoginError
    data class Server(val code: Int?, val message: String?) : LoginError
    data class Validation(val field: String?, val message: String?) : LoginError
    data class Database(val message: String?) : LoginError
    data class Unknown(val throwable: Throwable?) : LoginError
}

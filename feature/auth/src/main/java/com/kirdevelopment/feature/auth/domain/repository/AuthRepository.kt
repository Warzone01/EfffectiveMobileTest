package com.kirdevelopment.feature.auth.domain.repository

import com.kirdevelopment.core.common.result.AppResult

/**
 * Контракт репозитория авторизации.
 * Инкапсулирует логику входа и управления сессией.
 */
interface AuthRepository {
    /**
     * Выполнить вход с указанными учётными данными.
     * Возвращает AppResult<Unit> — успех означает, что токен получен и сохранён.
     */
    suspend fun login(email: String, password: String): AppResult<Unit>

    /**
     * Проверить, авторизован ли пользователь в данный момент.
     */
    fun isAuthorized(): Boolean

    /**
     * Выйти из аккаунта (очистить токен/сессию).
     */
    fun logout()
}

package com.kirdevelopment.feature.auth.data.repository

import com.kirdevelopment.core.common.error.AppError
import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация AuthRepository.
 * Пока заглушка — в будущем будет вызывать AuthApi и сохранять токен.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun login(email: String, password: String): AppResult<Unit> {
        // TODO: вызвать AuthApi.login(email, password), сохранить токен
        return AppResult.Success(Unit)
    }

    override fun isAuthorized(): Boolean {
        // TODO: проверить наличие сохранённого токена
        return false
    }

    override fun logout() {
        // TODO: очистить токен/сессию
    }
}

package com.kirdevelopment.feature.auth.domain.usecase

import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case для выполнения входа.
 * Делегирует вызов AuthRepository и возвращает типизированный AppResult.
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AppResult<Unit> {
        return authRepository.login(email, password)
    }
}

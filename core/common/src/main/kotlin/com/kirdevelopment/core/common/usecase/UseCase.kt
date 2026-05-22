package com.kirdevelopment.core.common.usecase

import com.kirdevelopment.core.common.result.AppResult
import kotlinx.coroutines.flow.Flow

interface UseCase<in P, R> {
    suspend operator fun invoke(params: P): AppResult<R>
}

interface FlowUseCase<in P, R> {
    operator fun invoke(params: P): Flow<R>
}

object NoParams

package com.kirdevelopment.core.common.extensions

import com.kirdevelopment.core.common.error.AppError
import com.kirdevelopment.core.common.result.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Стандартизирует обработку исключений в Flow цепочках.
 */
fun <T> Flow<T>.asResult(): Flow<AppResult<T>> {
    return map<T, AppResult<T>> { value ->
        AppResult.Success(value)
    }.catch { throwable ->
        emit(AppResult.Error(AppError.Unknown(throwable)))
    }
}

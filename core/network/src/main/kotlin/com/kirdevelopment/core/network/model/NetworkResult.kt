package com.kirdevelopment.core.network.model

import com.kirdevelopment.core.common.error.AppError

sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Error(val error: AppError) : NetworkResult<Nothing>
}

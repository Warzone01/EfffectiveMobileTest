package com.kirdevelopment.core.network.call

import com.kirdevelopment.core.network.error.ErrorHandler
import com.kirdevelopment.core.network.model.NetworkResult

interface SafeApiCall {
    suspend fun <T> execute(apiCall: suspend () -> T): NetworkResult<T>
}

class SafeApiCallImpl(
    private val errorHandler: ErrorHandler
) : SafeApiCall {

    override suspend fun <T> execute(apiCall: suspend () -> T): NetworkResult<T> {
        return try {
            NetworkResult.Success(apiCall())
        } catch (throwable: Throwable) {
            NetworkResult.Error(errorHandler.handle(throwable))
        }
    }
}

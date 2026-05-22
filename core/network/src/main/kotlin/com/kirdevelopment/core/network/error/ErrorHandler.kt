package com.kirdevelopment.core.network.error

import com.kirdevelopment.core.common.error.AppError
import java.io.IOException
import kotlinx.serialization.json.Json
import retrofit2.HttpException

interface ErrorHandler {
    fun handle(throwable: Throwable): AppError
}

/**
 * Общая точка маппинга сетевых ошибок в AppError.
 * Такая изоляция упрощает повторное использование и замену парсинга при расширении API.
 */
class DefaultErrorHandler(
    private val json: Json
) : ErrorHandler {

    override fun handle(throwable: Throwable): AppError {
        return when (throwable) {
            is HttpException -> mapHttpException(throwable)
            is IOException -> AppError.Network(message = throwable.message)
            else -> AppError.Unknown(throwable)
        }
    }

    private fun mapHttpException(exception: HttpException): AppError {
        val code = exception.code()
        val rawError = exception.response()?.errorBody()?.string()
        val parsedMessage = parseErrorMessage(rawError)
        return if (code in 500..599) {
            AppError.Server(code = code, message = parsedMessage)
        } else {
            AppError.Network(code = code, message = parsedMessage)
        }
    }

    private fun parseErrorMessage(rawError: String?): String? {
        if (rawError.isNullOrBlank()) return null
        return runCatching {
            json.decodeFromString(ErrorResponse.serializer(), rawError).message
        }.getOrNull()
    }
}

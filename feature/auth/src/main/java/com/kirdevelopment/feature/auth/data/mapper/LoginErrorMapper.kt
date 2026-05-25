package com.kirdevelopment.feature.auth.data.mapper

import com.kirdevelopment.core.common.error.AppError
import com.kirdevelopment.core.common.mapper.Mapper
import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.feature.auth.presentation.model.LoginError

/**
 * Маппер ошибок авторизации в UiText для отображения.
 */
class LoginErrorMapper : Mapper<AppError, LoginError> {
    override fun map(input: AppError): LoginError {
        return when (input) {
            is AppError.Network -> LoginError.Network(input.code, input.message)
            is AppError.Server -> LoginError.Server(input.code, input.message)
            is AppError.Validation -> LoginError.Validation(input.field, input.message)
            is AppError.Database -> LoginError.Database(input.message)
            is AppError.Unknown -> LoginError.Unknown(input.throwable)
        }
    }
}

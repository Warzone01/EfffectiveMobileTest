package com.kirdevelopment.core.network.model

import kotlinx.serialization.Serializable

/**
 * Базовая модель серверного ответа.
 * Нужна как расширяемый контейнер под возможные единые поля API.
 */
@Serializable
data class BaseApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val errors: Map<String, List<String>>? = null
)

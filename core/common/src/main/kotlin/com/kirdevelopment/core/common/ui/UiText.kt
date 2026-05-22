package com.kirdevelopment.core.common.ui

/**
 * Абстракция текста для UI, чтобы domain/data не зависели от Android Resources.
 */
sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class Resource(val resId: Int, val args: List<Any> = emptyList()) : UiText
}

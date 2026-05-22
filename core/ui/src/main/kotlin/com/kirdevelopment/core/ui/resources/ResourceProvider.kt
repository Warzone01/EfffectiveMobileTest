package com.kirdevelopment.core.ui.resources

import android.content.Context
import com.kirdevelopment.core.common.ui.UiText
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ResourceProvider {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg args: Any): String
    fun resolve(uiText: UiText): String
}

/**
 * Важный инфраструктурный слой между presentation и android resources.
 * Позволяет тестировать ViewModel без Context и держать UiText платформенно-независимым.
 */
class AndroidResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceProvider {
    override fun getString(resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg args: Any): String = context.getString(resId, *args)

    override fun resolve(uiText: UiText): String {
        return when (uiText) {
            is UiText.Dynamic -> uiText.value
            is UiText.Resource -> context.getString(uiText.resId, *uiText.args.toTypedArray())
        }
    }
}

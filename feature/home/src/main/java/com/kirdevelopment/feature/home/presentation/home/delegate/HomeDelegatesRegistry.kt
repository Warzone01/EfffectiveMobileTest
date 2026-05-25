package com.kirdevelopment.feature.home.presentation.home.delegate

import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class HomeDelegatesRegistry @Inject constructor(
    headerDelegate: HeaderItemDelegate,
    courseDelegate: CourseItemDelegate
) {
    private val delegates: List<HomeItemDelegate<out HomeAdapterItem>> = listOf(
        headerDelegate,
        courseDelegate
    )

    fun getViewType(item: HomeAdapterItem): Int {
        return delegates.firstOrNull { it.isForItem(item) }?.viewType
            ?: error("No delegate for item type: ${item::class.simpleName}")
    }
}

package com.kirdevelopment.feature.home.presentation.home.delegate

import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class HomeDelegatesRegistry @Inject constructor(
    courseDelegate: CourseItemDelegate,
    loadingDelegate: LoadingItemDelegate,
    errorDelegate: ErrorItemDelegate,
    emptyDelegate: EmptyItemDelegate
) {
    private val delegates: List<HomeItemDelegate<out HomeAdapterItem>> = listOf(
        courseDelegate,
        loadingDelegate,
        errorDelegate,
        emptyDelegate
    )

    fun getViewType(item: HomeAdapterItem): Int {
        return delegates.firstOrNull { it.isForItem(item) }?.viewType
            ?: error("No delegate for item type: ${item::class.simpleName}")
    }

    fun getDelegateByViewType(viewType: Int): HomeItemDelegate<out HomeAdapterItem> {
        return delegates.firstOrNull { it.isForViewType(viewType) }
            ?: error("No delegate for viewType: $viewType")
    }
}

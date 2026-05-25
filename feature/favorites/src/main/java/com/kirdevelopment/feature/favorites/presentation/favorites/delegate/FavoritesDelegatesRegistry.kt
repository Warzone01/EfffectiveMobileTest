package com.kirdevelopment.feature.favorites.presentation.favorites.delegate

import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem
import javax.inject.Inject

class FavoritesDelegatesRegistry @Inject constructor(
    courseDelegate: FavoriteCourseItemDelegate,
    loadingDelegate: FavoritesLoadingItemDelegate,
    emptyDelegate: FavoritesEmptyItemDelegate,
    errorDelegate: FavoritesErrorItemDelegate
) {
    private val delegates: List<FavoritesItemDelegate<out FavoritesAdapterItem>> = listOf(
        courseDelegate,
        loadingDelegate,
        emptyDelegate,
        errorDelegate
    )

    fun getViewType(item: FavoritesAdapterItem): Int {
        return delegates.firstOrNull { it.isForItem(item) }?.viewType
            ?: error("No delegate for item type: ${item::class.simpleName}")
    }
}

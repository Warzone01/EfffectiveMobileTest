package com.kirdevelopment.feature.favorites.presentation.favorites.delegate

import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem

interface FavoritesItemDelegate<T : FavoritesAdapterItem> {
    val viewType: Int
    fun isForItem(item: FavoritesAdapterItem): Boolean

    fun isForViewType(inputViewType: Int): Boolean {
        return viewType == inputViewType
    }
}

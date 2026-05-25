package com.kirdevelopment.feature.favorites.presentation.favorites.delegate

import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem
import javax.inject.Inject

class FavoritesEmptyItemDelegate @Inject constructor() : FavoritesItemDelegate<FavoritesAdapterItem.EmptyItem> {
    override val viewType: Int = FavoritesAdapterItem.VIEW_TYPE_EMPTY

    override fun isForItem(item: FavoritesAdapterItem): Boolean {
        return item is FavoritesAdapterItem.EmptyItem
    }
}

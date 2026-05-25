package com.kirdevelopment.feature.favorites.presentation.favorites.delegate

import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem
import javax.inject.Inject

class FavoritesLoadingItemDelegate @Inject constructor() : FavoritesItemDelegate<FavoritesAdapterItem.LoadingItem> {
    override val viewType: Int = FavoritesAdapterItem.VIEW_TYPE_LOADING

    override fun isForItem(item: FavoritesAdapterItem): Boolean {
        return item is FavoritesAdapterItem.LoadingItem
    }
}

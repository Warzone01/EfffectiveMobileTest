package com.kirdevelopment.feature.favorites.presentation.favorites.delegate

import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem
import javax.inject.Inject

class FavoritesErrorItemDelegate @Inject constructor() : FavoritesItemDelegate<FavoritesAdapterItem.ErrorItem> {
    override val viewType: Int = FavoritesAdapterItem.VIEW_TYPE_ERROR

    override fun isForItem(item: FavoritesAdapterItem): Boolean {
        return item is FavoritesAdapterItem.ErrorItem
    }
}

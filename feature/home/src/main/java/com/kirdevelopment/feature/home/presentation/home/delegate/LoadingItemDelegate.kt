package com.kirdevelopment.feature.home.presentation.home.delegate

import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class LoadingItemDelegate @Inject constructor() : HomeItemDelegate<HomeAdapterItem.LoadingItem> {
    override val viewType: Int = HomeAdapterItem.VIEW_TYPE_LOADING

    override fun isForItem(item: HomeAdapterItem): Boolean {
        return item is HomeAdapterItem.LoadingItem
    }
}

package com.kirdevelopment.feature.home.presentation.home.delegate

import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class EmptyItemDelegate @Inject constructor() : HomeItemDelegate<HomeAdapterItem.EmptyItem> {
    override val viewType: Int = HomeAdapterItem.VIEW_TYPE_EMPTY

    override fun isForItem(item: HomeAdapterItem): Boolean {
        return item is HomeAdapterItem.EmptyItem
    }
}

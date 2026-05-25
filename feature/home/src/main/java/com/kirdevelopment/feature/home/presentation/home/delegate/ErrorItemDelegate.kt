package com.kirdevelopment.feature.home.presentation.home.delegate

import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class ErrorItemDelegate @Inject constructor() : HomeItemDelegate<HomeAdapterItem.ErrorItem> {
    override val viewType: Int = HomeAdapterItem.VIEW_TYPE_ERROR

    override fun isForItem(item: HomeAdapterItem): Boolean {
        return item is HomeAdapterItem.ErrorItem
    }
}

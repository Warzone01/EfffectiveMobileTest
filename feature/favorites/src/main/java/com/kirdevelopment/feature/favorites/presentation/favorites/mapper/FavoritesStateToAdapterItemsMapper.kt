package com.kirdevelopment.feature.favorites.presentation.favorites.mapper

import com.kirdevelopment.core.common.mapper.Mapper
import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.feature.favorites.presentation.favorites.FavoritesScreenState
import com.kirdevelopment.feature.favorites.presentation.favorites.FavoritesUiState
import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem
import javax.inject.Inject

class FavoritesStateToAdapterItemsMapper @Inject constructor() : Mapper<FavoritesUiState, List<FavoritesAdapterItem>> {
    override fun map(input: FavoritesUiState): List<FavoritesAdapterItem> {
        return when (input.screenState) {
            FavoritesScreenState.Loading -> listOf(FavoritesAdapterItem.LoadingItem)
            FavoritesScreenState.Content -> input.items
            FavoritesScreenState.Empty -> listOf(FavoritesAdapterItem.EmptyItem(message = "Пока нет избранных курсов"))
            is FavoritesScreenState.Error -> listOf(FavoritesAdapterItem.ErrorItem(message = input.screenState.message.asPlainText()))
        }
    }

    private fun UiText.asPlainText(): String {
        return when (this) {
            is UiText.Dynamic -> value
            is UiText.Resource -> "Не удалось загрузить избранное"
        }
    }
}

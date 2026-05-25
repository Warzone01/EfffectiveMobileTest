package com.kirdevelopment.feature.favorites.presentation.favorites.mapper

import com.kirdevelopment.core.common.mapper.Mapper
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
            FavoritesScreenState.Error -> listOf(FavoritesAdapterItem.ErrorItem(message = "Не удалось загрузить избранное"))
        }
    }
}

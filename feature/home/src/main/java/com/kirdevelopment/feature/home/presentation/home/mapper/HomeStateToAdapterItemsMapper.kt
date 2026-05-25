package com.kirdevelopment.feature.home.presentation.home.mapper

import com.kirdevelopment.core.common.mapper.Mapper
import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.feature.home.presentation.home.HomeUiState
import com.kirdevelopment.feature.home.presentation.home.ScreenState
import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class HomeStateToAdapterItemsMapper @Inject constructor() : Mapper<HomeUiState, List<HomeAdapterItem>> {
    override fun map(input: HomeUiState): List<HomeAdapterItem> {
        return when (val state = input.screenState) {
            ScreenState.Loading -> listOf(HomeAdapterItem.LoadingItem)
            ScreenState.Empty -> listOf(HomeAdapterItem.EmptyItem(message = "Курсы не найдены"))
            is ScreenState.Error -> listOf(HomeAdapterItem.ErrorItem(message = state.message.asPlainText()))
            ScreenState.Content -> input.items
        }
    }

    private fun UiText.asPlainText(): String {
        return when (this) {
            is UiText.Dynamic -> value
            is UiText.Resource -> "Ошибка"
        }
    }
}

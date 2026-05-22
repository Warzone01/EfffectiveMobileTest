package com.kirdevelopment.domain.usecase

import com.kirdevelopment.core.common.usecase.FlowUseCase
import com.kirdevelopment.core.common.usecase.NoParams
import com.kirdevelopment.core.model.favorite.FavoriteCourse
import com.kirdevelopment.domain.repository.FavoritesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : FlowUseCase<NoParams, List<FavoriteCourse>> {
    override fun invoke(params: NoParams): Flow<List<FavoriteCourse>> {
        return favoritesRepository.observeFavorites()
    }
}

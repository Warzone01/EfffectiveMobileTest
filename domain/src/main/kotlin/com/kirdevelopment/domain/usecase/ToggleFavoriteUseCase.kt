package com.kirdevelopment.domain.usecase

import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.core.common.usecase.UseCase
import com.kirdevelopment.domain.repository.FavoritesRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : UseCase<Long, Unit> {
    override suspend fun invoke(params: Long): AppResult<Unit> {
        return runCatching {
            favoritesRepository.toggleFavorite(params)
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = { AppResult.Error(com.kirdevelopment.core.common.error.AppError.Unknown(it)) }
        )
    }
}

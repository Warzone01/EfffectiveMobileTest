package com.kirdevelopment.data.di

import com.kirdevelopment.data.local.FavoritesLocalDataSource
import com.kirdevelopment.data.local.FavoritesLocalDataSourceImpl
import com.kirdevelopment.data.repository.FavoritesRepositoryImpl
import com.kirdevelopment.domain.repository.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataLocalModule {

    @Binds
    @Singleton
    abstract fun bindFavoritesLocalDataSource(
        impl: FavoritesLocalDataSourceImpl
    ): FavoritesLocalDataSource

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        impl: FavoritesRepositoryImpl
    ): FavoritesRepository
}

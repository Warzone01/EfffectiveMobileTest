package com.kirdevelopment.core.database.di

import android.content.Context
import androidx.room.Room
import com.kirdevelopment.core.database.AppDatabase
import com.kirdevelopment.core.database.DatabaseConstants
import com.kirdevelopment.core.database.dao.FavoriteCoursesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        )
            // Для тестового проекта используем деструктивную миграцию как временный
            // инфраструктурный дефолт, позже заменяется явными Migration объектами.
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFavoriteCoursesDao(database: AppDatabase): FavoriteCoursesDao {
        return database.favoriteCoursesDao()
    }
}

package com.kirdevelopment.data.di

import com.kirdevelopment.data.remote.CoursesRemoteDataSource
import com.kirdevelopment.data.remote.CoursesRemoteDataSourceImpl
import com.kirdevelopment.data.remote.api.CoursesApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DataRemoteRetrofitModule {

    @Provides
    @Singleton
    fun provideCoursesApi(retrofit: Retrofit): CoursesApi {
        return retrofit.create(CoursesApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataRemoteBindModule {

    @Binds
    @Singleton
    abstract fun bindCoursesRemoteDataSource(
        impl: CoursesRemoteDataSourceImpl
    ): CoursesRemoteDataSource
}

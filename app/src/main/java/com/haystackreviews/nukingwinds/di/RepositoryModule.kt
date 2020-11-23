package com.haystackreviews.nukingwinds.di

import com.haystackreviews.nukingwinds.data.WeatherRepository
import com.haystackreviews.nukingwinds.data.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(impl: WeatherRepositoryImpl): WeatherRepository
}
package com.haystackreviews.nukingwinds.di

import android.content.Context
import androidx.room.Room
import com.haystackreviews.nukingwinds.data.database.WeatherDao
import com.haystackreviews.nukingwinds.data.database.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module

object DatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
                context,
                WeatherDatabase::class.java, "weather.db"
        ).build()
    }

    @Provides
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao {
        return database.weatherDao()
    }
}
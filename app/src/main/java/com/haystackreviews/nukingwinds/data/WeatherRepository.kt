package com.haystackreviews.nukingwinds.data

import com.haystackreviews.nukingwinds.data.database.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(lat: Double, lon: Double): Flow<Weather>
}
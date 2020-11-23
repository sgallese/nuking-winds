package com.haystackreviews.nukingwinds.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["lat", "lon"])
data class Weather(
        val lat: Double,
        val lon: Double,
        @ColumnInfo(name = "wind_speed") val windSpeed: Double,
        @ColumnInfo(name = "wind_gust") val windGust: Double?,
        @ColumnInfo(name = "description") val description: String?
)
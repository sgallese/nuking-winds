package com.haystackreviews.nukingwinds.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.haystackreviews.nukingwinds.data.database.Weather
import com.haystackreviews.nukingwinds.data.database.WeatherDao

@Database(entities = arrayOf(Weather::class), version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
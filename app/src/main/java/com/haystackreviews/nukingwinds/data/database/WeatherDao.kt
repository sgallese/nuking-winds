package com.haystackreviews.nukingwinds.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE lat LIKE :lat AND " +
            "lon LIKE :lon LIMIT 1")
    fun findByLatLon(lat: Double, lon: Double): Weather?

    @Insert
    fun insertAll(vararg oneCallEntities: Weather)

    @Delete
    fun delete(oneCallEntity: Weather)
}
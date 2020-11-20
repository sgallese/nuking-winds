package com.haystackreviews.nukingwinds.sdk

import android.app.Application
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@Entity(primaryKeys = ["lat", "lon"])
data class Weather(
    val lat: Double,
    val lon: Double,
    @ColumnInfo(name = "wind_speed") val windSpeed: Double,
    @ColumnInfo(name = "wind_gust") val windGust: Double?,
    @ColumnInfo(name = "description") val description: String?
)

class WeatherRepository(private val application: Application, private val api: WeatherApi) {

    fun getWeather(lat: Double, lon: Double): Flow<Weather> {
        return flow {
            val db = Room.databaseBuilder(
                    application,
                    WeatherDatabase::class.java, "weather"
            ).build()
            val dbResult = db.weatherDao().findByLatLon(lat, lon)
            if (dbResult == null) {
                val networkResult = WeatherNetwork.getWeather(api, lat, lon)
                emit(networkResult)
                db.weatherDao().insertAll(networkResult)
            } else {
                emit(dbResult)
            }
        }.flowOn(Dispatchers.IO)
    }

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

    @Database(entities = arrayOf(Weather::class), version = 1)
    abstract class WeatherDatabase : RoomDatabase() {
        abstract fun weatherDao(): WeatherDao
    }
}
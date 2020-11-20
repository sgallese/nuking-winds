package com.haystackreviews.nukingwinds.ui.main

import android.app.Application
import androidx.room.*
import com.squareup.moshi.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.lang.IllegalStateException


data class OneCall(@field:Json(name = "current") val current: Current, @field:Json(name = "alerts") val alerts: List<Alert>?)

data class Current(@field:Json(name = "wind_speed") val windSpeed: Double, @field:Json(name = "wind_gust") val windGust: Double?)

data class Alert(@field:Json(name = "description") val description: String)

interface OpenWeatherMapAPI {
    @GET("onecall?appid=13bc561649c9f354c290ff5da48d3aab")
    suspend fun oneCall(@retrofit2.http.Query("lat") lat: Double, @retrofit2.http.Query("lon") lon: Double): OneCall
}

val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

class OneCallRepository(val application: Application, private val api: OpenWeatherMapAPI) {

    fun getOneCall(lat: Double, lon: Double): Flow<OneCall> {
        return flow {
            val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java, "onecall"
            ).build()
            var dbResult = db.oneCallDao().findByLatLon(lat, lon)
            if (dbResult == null) {
                val oneCall = api.oneCall(lat, lon)
                db.oneCallDao().insertAll(OneCallEntity(lat, lon, oneCall.current.windSpeed, oneCall.current.windGust, oneCall.alerts?.getOrNull(0)?.description))
                dbResult = db.oneCallDao().findByLatLon(lat, lon)
            }
            val result = dbResult
                    ?: throw IllegalStateException("Unable to find result with lat: $lat lon: $lon")
            val description = result.description
            val alerts: List<Alert>? = if (description == null) {
                null
            } else {
                listOf(Alert(description))
            }
            emit(OneCall(Current(result.windSpeed, result.windGust), alerts))
        }.flowOn(Dispatchers.IO)
    }
}

@Entity(primaryKeys = ["lat", "lon"])
data class OneCallEntity(
        val lat: Double,
        val lon: Double,
        @ColumnInfo(name = "wind_speed") val windSpeed: Double,
        @ColumnInfo(name = "wind_gust") val windGust: Double?,
        @ColumnInfo(name = "description") val description: String?
)

@Dao
interface OneCallDao {
    @Query("SELECT * FROM oneCallEntity WHERE lat LIKE :lat AND " +
            "lon LIKE :lon LIMIT 1")
    fun findByLatLon(lat: Double, lon: Double): OneCallEntity?

    @Insert
    fun insertAll(vararg oneCallEntities: OneCallEntity)

    @Delete
    fun delete(oneCallEntity: OneCallEntity)
}

@Database(entities = arrayOf(OneCallEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun oneCallDao(): OneCallDao
}
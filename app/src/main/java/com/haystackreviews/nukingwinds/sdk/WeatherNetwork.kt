package com.haystackreviews.nukingwinds.sdk

import com.squareup.moshi.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface WeatherApi {
    @GET("onecall?appid=13bc561649c9f354c290ff5da48d3aab")
    suspend fun oneCall(
        @retrofit2.http.Query("lat") lat: Double,
        @retrofit2.http.Query("lon") lon: Double
    ): WeatherNetwork.OneCallNetworkResponse
}

object WeatherNetwork {

    suspend fun getWeather(api: WeatherApi, lat: Double, lon: Double): Weather {
        val response = api.oneCall(lat, lon)
        return Weather(
            lat,
            lon,
            response.current.windSpeed,
            response.current.windGust,
            response.alerts?.getOrNull(0)?.description
        )
    }

    val api by lazy {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        retrofit.create(WeatherApi::class.java)
    }

    data class OneCallNetworkResponse(
        @field:Json(name = "current") val current: CurrentNetworkResponse,
        @field:Json(name = "alerts") val alerts: List<AlertNetworkResponse>?
    )

    data class CurrentNetworkResponse(
        @field:Json(name = "wind_speed") val windSpeed: Double,
        @field:Json(name = "wind_gust") val windGust: Double?
    )

    data class AlertNetworkResponse(@field:Json(name = "description") val description: String)
}
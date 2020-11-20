package com.haystackreviews.nukingwinds.ui.main

import com.squareup.moshi.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


data class OneCall(@field:Json(name = "current") val current: Current, @field:Json(name = "alerts") val alerts: List<Alert>?)

data class Current(@field:Json(name = "wind_speed") val windSpeed: Double, @field:Json(name = "wind_gust") val windGust: Double?)

data class Alert(@field:Json(name = "description") val description: String)

interface OpenWeatherMapAPI {
    @GET("onecall?lat=38.056017&lon=-121.788563&appid=13bc561649c9f354c290ff5da48d3aab")
    suspend fun oneCall(): OneCall
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl("https://api.openweathermap.org/data/2.5/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()
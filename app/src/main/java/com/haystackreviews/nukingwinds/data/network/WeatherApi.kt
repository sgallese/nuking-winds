package com.haystackreviews.nukingwinds.data.network

import com.haystackreviews.nukingwinds.data.network.response.OneCall
import retrofit2.http.GET

interface WeatherApi {
    @GET("onecall?appid=13bc561649c9f354c290ff5da48d3aab")
    suspend fun oneCall(
        @retrofit2.http.Query("lat") lat: Double,
        @retrofit2.http.Query("lon") lon: Double
    ): OneCall
}
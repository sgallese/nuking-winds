package com.haystackreviews.nukingwinds.data.network.response

import com.squareup.moshi.Json

data class Current(
        @field:Json(name = "wind_speed") val windSpeed: Double,
        @field:Json(name = "wind_gust") val windGust: Double?
)
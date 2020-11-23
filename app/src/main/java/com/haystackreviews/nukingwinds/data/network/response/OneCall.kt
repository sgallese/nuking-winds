package com.haystackreviews.nukingwinds.data.network.response

import com.squareup.moshi.Json

data class OneCall(
        @field:Json(name = "current") val current: Current,
        @field:Json(name = "alerts") val alerts: List<Alert>?
)
package com.haystackreviews.nukingwinds.data.network.response

import com.squareup.moshi.Json

data class Alert(@field:Json(name = "description") val description: String)
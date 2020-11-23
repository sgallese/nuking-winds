package com.haystackreviews.nukingwinds.ui.main

sealed class WeatherState
object Loading : WeatherState()
data class Content(val windSpeed: String, val windGust: String, val alertDescription: String) :
        WeatherState()
data class Error(val message: String) : WeatherState()
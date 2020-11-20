package com.haystackreviews.nukingwinds.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.flow

class MainViewModel : ViewModel() {

    val oneCallState = flow {
        emit(Loading)
        val service: OpenWeatherMapAPI = retrofit.create(OpenWeatherMapAPI::class.java)
        var contentOrError: OneCallState = Error("Unable to fetch weather data")
        runCatching { service.oneCall() }
                .onSuccess {
                    val windGustString = if (it.current.windGust == null) {
                        "N/A"
                    } else {
                        it.current.windGust.toString()
                    }

                    val alertDescriptionString = if (it.alerts == null) {
                        "No alerts found"
                    } else {
                        it.alerts[0].description
                    }
                    contentOrError = Content(it.current.windSpeed.toString(), windGustString, alertDescriptionString)
                }
                .onFailure {
                    it.message?.let { message -> contentOrError = Error(message) }
                }
        emit(contentOrError)
    }.asLiveData()
}

sealed class OneCallState
object Loading : OneCallState()
data class Content(val windSpeed: String, val windGust: String, val alertDescription: String) : OneCallState()
data class Error(val message: String) : OneCallState()
package com.haystackreviews.nukingwinds.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.haystackreviews.nukingwinds.data.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class WeatherViewModel @ViewModelInject constructor(private val repository: WeatherRepository) :
        ViewModel() {

    @ExperimentalCoroutinesApi
    val weatherState =
            flow<WeatherState> {
                repository.getWeather(38.056017, -121.788563).collect { weather ->
                    val windGustString = weather.windGust?.toString() ?: "N/A"
                    val alertDescriptionString = weather.description ?: "No alerts found"
                    emit(Content(weather.windSpeed.toString(), windGustString, alertDescriptionString))
                }
            }.onStart {
                emit(Loading)
            }.catch {
                val message = it.message ?: "Unable to fetch weather data"
                emit(Error(message))
            }.asLiveData()
}


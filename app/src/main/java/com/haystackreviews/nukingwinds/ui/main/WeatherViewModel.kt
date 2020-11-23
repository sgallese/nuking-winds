package com.haystackreviews.nukingwinds.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.haystackreviews.nukingwinds.data.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WeatherViewModel @ViewModelInject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    private val _weatherState = MutableLiveData<WeatherState>()
    val weatherState: LiveData<WeatherState>
        get() = _weatherState

    fun updateWeather(lat: String, lon: String) = viewModelScope.launch {

        _weatherState.value = Loading

        val latDouble = lat.toDoubleOrNull()
        val lonDouble = lon.toDoubleOrNull()
        if (latDouble == null || lonDouble == null) {
            _weatherState.value = Error("Not a valid Latitude/Longitude")
        } else {
            try {
                repository.getWeather(latDouble, lonDouble).collect { weather ->
                    val windGustString = weather.windGust?.toString() ?: "N/A"
                    val alertDescriptionString = weather.description ?: "No alerts found"
                    _weatherState.value =
                        Content(
                            lat,
                            lon,
                            weather.windSpeed.toString(),
                            windGustString,
                            alertDescriptionString
                        )
                }
            } catch (e: Exception) {
                val message = e.message ?: "Unable to fetch weather data"
                _weatherState.value = Error(message)
            }
        }
    }
}


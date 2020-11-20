package com.haystackreviews.nukingwinds.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.haystackreviews.nukingwinds.sdk.WeatherNetwork
import com.haystackreviews.nukingwinds.sdk.WeatherRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository = WeatherRepository(application, WeatherNetwork.api)

    val oneCallState =
            flow<WeatherState> {
                repository.getWeather(38.056017, -121.788563).collect { weather ->
                    val windGustString = weather.windGust?.toString() ?: "N/A"
                    val alertDescriptionString = weather.description ?: "No alerts found"
                    emit(Content(weather.windSpeed.toString(), windGustString, alertDescriptionString))
                }
            }
                    .onStart {
                        emit(Loading)
                    }
                    .catch {
                        val message = it.message ?: "Unable to fetch weather data"
                        emit(Error(message))
                    }
                    .asLiveData()
}

sealed class WeatherState
object Loading : WeatherState()
data class Content(val windSpeed: String, val windGust: String, val alertDescription: String) : WeatherState()
data class Error(val message: String) : WeatherState()


class Factory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct ViewModel")
    }
}
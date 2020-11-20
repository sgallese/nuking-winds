package com.haystackreviews.nukingwinds.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val api: OpenWeatherMapAPI = retrofit.create(OpenWeatherMapAPI::class.java)
    val repository = OneCallRepository(application, api)

    val oneCallState =
            flow<OneCallState> {
                repository.getOneCall(38.056017, -121.788563).collect { oneCall ->
                    val windGust = oneCall.current.windGust
                    val windGustString = windGust?.toString() ?: "N/A"
                    val alerts = oneCall.alerts
                    val alertDescriptionString = if (alerts == null) {
                        "No alerts found"
                    } else {
                        alerts[0].description
                    }
                    emit(Content(oneCall.current.windSpeed.toString(), windGustString, alertDescriptionString))
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

sealed class OneCallState
object Loading : OneCallState()
data class Content(val windSpeed: String, val windGust: String, val alertDescription: String) : OneCallState()
data class Error(val message: String) : OneCallState()


class Factory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct ViewModel")
    }
}
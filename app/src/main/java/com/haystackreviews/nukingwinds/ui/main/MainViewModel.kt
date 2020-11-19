package com.haystackreviews.nukingwinds.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {

    fun getWindSpeed() = liveData(Dispatchers.IO) {
        val service: OpenWeatherMapAPI = retrofit.create(OpenWeatherMapAPI::class.java)
        val oneCallResponse = service.oneCall()
        val windSpeed = oneCallResponse.current.windSpeed
        emit(windSpeed)
    }
}
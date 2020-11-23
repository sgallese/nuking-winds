package com.haystackreviews.nukingwinds.data

import com.haystackreviews.nukingwinds.data.database.Weather
import com.haystackreviews.nukingwinds.data.database.WeatherDao
import com.haystackreviews.nukingwinds.data.network.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: WeatherApi,
                                                private val dao: WeatherDao) : WeatherRepository {
    override fun getWeather(lat: Double, lon: Double): Flow<Weather> {
        return flow {
            val dbResult = getWeatherFromDatabase(dao, lat, lon)
            if (dbResult == null) {
                val networkResult = getWeatherFromNetwork(api, lat, lon)
                emit(networkResult)
                dao.insertAll(networkResult)
            } else {
                emit(dbResult)
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun getWeatherFromDatabase(dao: WeatherDao, lat: Double, lon: Double): Weather? {
        return dao.findByLatLon(lat, lon)
    }

    private suspend fun getWeatherFromNetwork(api: WeatherApi, lat: Double, lon: Double): Weather {
        val response = api.oneCall(lat, lon)
        return Weather(
                lat,
                lon,
                response.current.windSpeed,
                response.current.windGust,
                response.alerts?.getOrNull(0)?.description
        )
    }
}
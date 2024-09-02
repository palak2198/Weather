package com.example.weatherapp.domain.usecase

import com.example.weatherapp.R
import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.model.WeatherConditions
import com.example.weatherapp.data.model.WeatherConditionsItem
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.domain.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository, private val weatherDao: WeatherDao) {

    suspend fun getCityWeather(city: String?) = flow {
        try {
            val result = weatherRepository.getCityWeather(city)
            val mapData = mapData(result)
            weatherDao.insertWeatherData(mapData)
            emit(ResultWrapper.Success(mapData(result)))
        }
        catch(e: Exception) {
            emit(ResultWrapper.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getCityWeatherFromDb() = flow {
        try {
            val result = weatherDao.getWeatherConditions()
            emit(ResultWrapper.Success(result))
        }
        catch(e: Exception) {
            emit(ResultWrapper.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    fun mapData(response: WeatherResponse): WeatherConditions {

        val weatherConditions = arrayListOf<WeatherConditionsItem>()

        val feelsLike = response.main?.feelsLike ?: 0f
        val humidity = response.main?.humidity ?: 0
        val speed = response.wind?.speed ?: 0f
        val pressure = response.main?.pressure ?: 0
        val visibility = response.visibility ?: 0
        val seaLevel = response.main?.seaLevel ?: 0
        val name = response.name ?: ""
        val temp = response.main?.temp ?: 0f
        val main = response.weather?.getOrNull(0)?.main ?: ""
        val tempMin = response.main?.tempMin ?: 0f
        val tempMax = response.main?.tempMax ?: 0f
        val desc = response.weather?.getOrNull(0)?.description ?: ""
        val icon = response.weather?.getOrNull(0)?.icon ?: ""

        weatherConditions.add(WeatherConditionsItem(R.drawable.iv_feels_like,"Feels Like", "${feelsLike.toInt()}°", icon))
        weatherConditions.add(WeatherConditionsItem(R.drawable.iv_humidity,"Humidity", "$humidity %", icon))
        weatherConditions.add(WeatherConditionsItem(R.drawable.iv_wind,"Speed", "${speed.toInt()} km/h", icon))
        weatherConditions.add(WeatherConditionsItem(R.drawable.iv_pressure,"Pressure", "$pressure hPa", icon))
        weatherConditions.add(WeatherConditionsItem(R.drawable.iv_visibility,"Visibility", "${(visibility / 1000)} km", icon))
        weatherConditions.add(WeatherConditionsItem(R.drawable.iv_sea_level,"Sea Level", "$seaLevel hPa", icon))

        return WeatherConditions(name = name, temp = temp, main = main, tempMin = tempMin, tempMax = tempMax, description = desc, icon = icon, weatherConditionsItem = weatherConditions)
    }
}
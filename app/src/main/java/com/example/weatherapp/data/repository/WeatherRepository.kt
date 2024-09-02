package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.WeatherResponse
import retrofit2.Response

interface WeatherRepository {

    suspend fun getCityWeather(city: String?): WeatherResponse
}
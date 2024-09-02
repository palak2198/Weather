package com.example.weatherapp.data.repository

import com.example.weatherapp.data.api.ApiService
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor (private val apiService: ApiService): WeatherRepository {

    override suspend fun getCityWeather(city: String?) = apiService.cityWeather(city)
}
package com.example.weatherapp.data.api

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    suspend fun cityWeather(
        @Query("q") q: String?,
        @Query("units") units: String = Constants.WEATHER_UNIT,
        @Query("appid") appid: String = BuildConfig.weather_api_key
    ): WeatherResponse
}
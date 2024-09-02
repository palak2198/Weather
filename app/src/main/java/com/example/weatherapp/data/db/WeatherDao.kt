package com.example.weatherapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.model.WeatherConditions

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(weatherConditions: WeatherConditions)

    @Query("SELECT * FROM ${TableConstant.WEATHER_CONDITION_ENTITY}")
    fun getWeatherConditions(): WeatherConditions?
}
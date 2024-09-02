package com.example.weatherapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.data.model.WeatherConditions

@Database(
    entities = [WeatherConditions::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val weatherDao: WeatherDao

    companion object {
        const val DATABASE_NAME = "weather_db"
    }
}

object TableConstant {
    const val WEATHER_CONDITION_ENTITY = "weatherConditions"
}
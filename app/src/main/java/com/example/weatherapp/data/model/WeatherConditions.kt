package com.example.weatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.data.db.TableConstant

data class WeatherConditionsItem(

    val image: Int,
    val text: String,
    val value: String,
    val iconCode: String
)

@Entity(tableName = TableConstant.WEATHER_CONDITION_ENTITY)
data class WeatherConditions(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val name: String,

    val temp: Float,

    val main: String,

    val tempMin: Float,

    val tempMax: Float,

    val description: String,

    val icon: String,

    val weatherConditionsItem: ArrayList<WeatherConditionsItem>
)
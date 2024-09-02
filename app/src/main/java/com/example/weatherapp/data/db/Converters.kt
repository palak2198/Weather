package com.example.weatherapp.data.db

import androidx.room.TypeConverter
import com.example.weatherapp.data.model.WeatherConditionsItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromArrayList(list: ArrayList<WeatherConditionsItem>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromString(value: String): ArrayList<WeatherConditionsItem> {
        val listType = object : TypeToken<ArrayList<WeatherConditionsItem>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
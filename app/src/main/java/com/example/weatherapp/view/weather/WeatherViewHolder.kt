package com.example.weatherapp.view.weather

import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherConditionsItem
import com.example.weatherapp.databinding.ItemWeatherConditionsBinding

class WeatherViewHolder(private val binding: ItemWeatherConditionsBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(weatherCondition: WeatherConditionsItem) {

        binding.ivWeather.setImageResource(weatherCondition.image)
        binding.tvWeatherCondition.text = weatherCondition.text
        binding.tvWeatherConditionDetail.text = weatherCondition.value

        val background = when (weatherCondition.iconCode) {
            "01d", "02d", "03d" -> R.color.metallic_seaweed
            "04d", "09d", "10d", "11d" -> R.color.charcoal
            "13d", "50d" -> R.color.transparent_black
            else -> R.color.black
        }

        binding.llDesign.setBackgroundColor(background)
    }
}
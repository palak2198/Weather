package com.example.weatherapp.view.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.WeatherConditionsItem
import com.example.weatherapp.databinding.ItemWeatherConditionsBinding
import javax.inject.Inject

class WeatherAdapter @Inject constructor(): RecyclerView.Adapter<WeatherViewHolder>() {
    private var weatherConditions: ArrayList<WeatherConditionsItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            ItemWeatherConditionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weatherConditions.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherConditions[position])
    }

    fun addAll(list: List<WeatherConditionsItem>) {
        weatherConditions.clear()
        weatherConditions.addAll(list)
        notifyDataSetChanged()
    }

}
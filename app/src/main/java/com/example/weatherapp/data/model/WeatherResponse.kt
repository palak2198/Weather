package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

	@SerializedName("visibility")
	val visibility: Int? = null,

	@SerializedName("timezone")
	val timezone: Int? = null,

	@SerializedName("main")
	val main: Main? = null,

	@SerializedName("clouds")
	val clouds: Clouds? = null,

	@SerializedName("sys")
	val sys: Sys? = null,

	@SerializedName("dt")
	val dt: Int? = null,

	@SerializedName("coord")
	val coord: Coord? = null,

	@SerializedName("weather")
	val weather: List<WeatherItem?>? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("cod")
	val cod: Int? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("base")
	val base: String? = null,

	@SerializedName("wind")
	val wind: Wind? = null
)

data class Main(

	@SerializedName("temp")
	val temp: Float? = null,

	@SerializedName("temp_min")
	val tempMin: Float? = null,

	@SerializedName("grnd_level")
	val grndLevel: Int? = null,

	@SerializedName("humidity")
	val humidity: Int? = null,

	@SerializedName("pressure")
	val pressure: Int? = null,

	@SerializedName("sea_level")
	val seaLevel: Int? = null,

	@SerializedName("feels_like")
	val feelsLike: Float? = null,

	@SerializedName("temp_max")
	val tempMax: Float? = null
)

data class WeatherItem(

	@SerializedName("icon")
	val icon: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("main")
	val main: String? = null,

	@SerializedName("id")
	val id: Int? = null
)

data class Sys(

	@SerializedName("country")
	val country: String? = null,

	@SerializedName("sunrise")
	val sunrise: Int? = null,

	@SerializedName("sunset")
	val sunset: Int? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("type")
	val type: Int? = null
)

data class Coord(

	@SerializedName("lon")
	val lon: Any? = null,

	@SerializedName("lat")
	val lat: Any? = null
)

data class Wind(

	@SerializedName("deg")
	val deg: Int? = null,

	@SerializedName("speed")
	val speed: Float? = null
)

data class Clouds(

	@SerializedName("all")
	val all: Int? = null
)

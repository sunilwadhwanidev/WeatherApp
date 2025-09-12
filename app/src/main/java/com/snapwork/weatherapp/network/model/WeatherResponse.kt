package com.snapwork.weatherapp.network.model

data class WeatherResponse(
    val name: String,
    val sys: Sys,
    val main: Main,
    val weather: List<WeatherItem>
)

data class Sys(
    val country: String?,
    val sunrise: Long,
    val sunset: Long
)

data class Main(
    val temp: Double
)

data class WeatherItem(
    val main: String,
    val description: String
)

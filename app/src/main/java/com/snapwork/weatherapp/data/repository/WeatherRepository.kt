package com.snapwork.weatherapp.data.repository

import com.snapwork.weatherapp.data.local.WeatherDao
import com.snapwork.weatherapp.data.local.WeatherEntity

class WeatherRepository(private val dao: WeatherDao) {

    suspend fun insertWeather(weather: WeatherEntity) {
        dao.insertWeather(weather)
    }

    fun getAllWeather() = dao.getAllWeather()
}

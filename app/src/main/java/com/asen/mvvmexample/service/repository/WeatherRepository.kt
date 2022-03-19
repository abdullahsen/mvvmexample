package com.asen.mvvmexample.service.repository

import com.asen.mvvmexample.service.api.WeatherService

class WeatherRepository(private val weatherService: WeatherService) {
    suspend fun getWeatherData() = weatherService.getWeather()
}
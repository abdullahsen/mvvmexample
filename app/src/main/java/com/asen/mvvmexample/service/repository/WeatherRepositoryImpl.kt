package com.asen.mvvmexample.service.repository

import com.asen.mvvmexample.service.api.WeatherService

class WeatherRepositoryImpl(private val weatherService: WeatherService) : WeatherRepository {
    override suspend fun getWeatherData() = weatherService.getWeather()
}
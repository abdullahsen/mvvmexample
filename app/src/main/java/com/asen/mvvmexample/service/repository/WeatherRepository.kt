package com.asen.mvvmexample.service.repository

import com.asen.mvvmexample.service.response.WeatherForecast

interface WeatherRepository {
    suspend fun getWeatherData(): WeatherForecast
}
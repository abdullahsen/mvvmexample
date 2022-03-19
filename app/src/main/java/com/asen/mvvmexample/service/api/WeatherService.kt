package com.asen.mvvmexample.service.api

import com.asen.mvvmexample.service.response.WeatherForecast
import retrofit2.http.GET

interface WeatherService {
    @GET("weather?q=London")
    suspend fun getWeather(): WeatherForecast
}
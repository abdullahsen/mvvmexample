package com.asen.mvvmexample.usecase.mapper

import com.asen.mvvmexample.model.WeatherModel
import com.asen.mvvmexample.service.response.WeatherForecast

class WeatherMapper {
    fun map(weatherData: WeatherForecast): WeatherModel {
        return WeatherModel(
            name = weatherData.name,
            temperature = weatherData.main.temp,
            maxTemperature = weatherData.main.tempMax,
            minTemperature = weatherData.main.tempMin
        )
    }
}
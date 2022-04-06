package com.asen.mvvmexample.usecase.mapper

import com.asen.mvvmexample.model.WeatherModel
import com.asen.mvvmexample.service.response.WeatherForecast
import org.junit.Assert.*
import org.junit.Test

class WeatherMapperTest {

    private val weatherMapper = WeatherMapper()

    @Test
    fun `verify weather api response is mapped as weather model correctly`() {
        val expectedWeatherModel = WeatherModel(
            name = "Amsterdam",
            temperature = 23.0,
            maxTemperature = 30.0,
            minTemperature = 15.0
        )

        val actualWeatherModel = weatherMapper.map(apiResponse)

        assertEquals(expectedWeatherModel, actualWeatherModel)
    }

    private val apiResponse = WeatherForecast(
        base = "",
        clouds = WeatherForecast.Clouds(0),
        cod = 0,
        coord = WeatherForecast.Coord(0.5, 0.5),
        dt = 0,
        id = 0,
        main = WeatherForecast.Main(0, 0, 23.0, 30.0, 15.0),
        name = "Amsterdam",
        sys = WeatherForecast.Sys("", 0, 0.0, 0, 0, 0),
        visibility = 0,
        weather = emptyList(),
        wind = WeatherForecast.Wind(0, 0.0)
    )

}
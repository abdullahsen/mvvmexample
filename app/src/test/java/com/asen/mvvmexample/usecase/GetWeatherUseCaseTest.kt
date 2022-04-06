package com.asen.mvvmexample.usecase

import com.asen.mvvmexample.MainCoroutineRule
import com.asen.mvvmexample.model.WeatherModel
import com.asen.mvvmexample.service.repository.WeatherRepository
import com.asen.mvvmexample.service.response.WeatherForecast
import com.asen.mvvmexample.usecase.mapper.WeatherMapper
import com.asen.mvvmexample.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetWeatherUseCaseTest {

    private lateinit var getWeatherUseCase: GetWeatherUseCase
    private val repository = mock<WeatherRepository>()
    private val mapper = mock<WeatherMapper>()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        getWeatherUseCase = GetWeatherUseCaseImpl(repository, mapper, Dispatchers.Main)
    }

    @Test
    fun `verify getWeatherUseCase's successful scenario`() = mainCoroutineRule.runBlockingTest {
        whenever(repository.getWeatherData()).thenReturn(apiResponse)
        whenever(mapper.map(any())).thenReturn(mappedWeatherModel)

        val expected = Result.OnSuccess(mappedWeatherModel)
        val actual = getWeatherUseCase.execute()

        assertEquals(expected, actual)
    }

    @Test
    fun `verify getWeatherUseCase returns Error when repository fails`() = mainCoroutineRule.runBlockingTest {
        whenever(repository.getWeatherData()).thenReturn(null)
        whenever(mapper.map(any())).thenReturn(mappedWeatherModel)

        val expected = Result.OnError
        val actual = getWeatherUseCase.execute()

        assertEquals(expected, actual)
    }

    @Test
    fun `verify getWeatherUseCase returns Error when mapper fails`() = mainCoroutineRule.runBlockingTest {
        whenever(repository.getWeatherData()).thenReturn(apiResponse)
        whenever(mapper.map(any())).thenReturn(null)

        val expected = Result.OnError
        val actual = getWeatherUseCase.execute()

        assertEquals(expected, actual)
    }


    private val apiResponse = WeatherForecast(
        base = "",
        clouds = WeatherForecast.Clouds(0),
        cod = 0,
        coord = WeatherForecast.Coord(0.0, 0.0),
        dt = 0,
        id = 0,
        main = WeatherForecast.Main(0, 0, 23.0, 30.0, 15.0),
        name = "Amsterdam",
        sys = WeatherForecast.Sys("", 0, 0.0, 0, 0, 0),
        visibility = 0,
        weather = emptyList(),
        wind = WeatherForecast.Wind(0, 0.0)
    )

    private val mappedWeatherModel = WeatherModel(
        name = "Amsterdam",
        temperature = 23.0,
        maxTemperature = 30.0,
        minTemperature = 15.0
    )
}
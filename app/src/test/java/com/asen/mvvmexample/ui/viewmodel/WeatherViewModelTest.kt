package com.asen.mvvmexample.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.asen.mvvmexample.MainCoroutineRule
import com.asen.mvvmexample.model.WeatherModel
import com.asen.mvvmexample.usecase.GetWeatherUseCase
import com.asen.mvvmexample.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class WeatherViewModelTest {
    private lateinit var weatherViewModel: WeatherViewModel
    private val useCase = mock<GetWeatherUseCase>()
    private val observer = mock<Observer<WeatherViewModelUIState>>()

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        weatherViewModel = WeatherViewModel(useCase)
        weatherViewModel.uiState.observeForever(observer)
    }

    @Test
    fun `verify successful response shows WeatherLoaded ui state`() =
        mainCoroutineRule.runBlockingTest {
            whenever(useCase.execute()).thenReturn(Result.OnSuccess(weatherModel))
            weatherViewModel.loadWeather()
            verify(observer).onChanged(WeatherViewModelUIState.ShowLoading)
            verify(observer).onChanged(WeatherViewModelUIState.WeatherLoaded(weatherModel))
        }

    @Test
    fun `verify unsuccessful response shows Error state`() = mainCoroutineRule.runBlockingTest {
        whenever(useCase.execute()).thenReturn(Result.OnError)
        weatherViewModel.loadWeather()
        verify(observer).onChanged(WeatherViewModelUIState.ShowLoading)
        verify(observer).onChanged(WeatherViewModelUIState.ShowError)
    }


    private val weatherModel = WeatherModel(
        name = "Amsterdam",
        temperature = 23.0,
        maxTemperature = 30.0,
        minTemperature = 15.0
    )

}
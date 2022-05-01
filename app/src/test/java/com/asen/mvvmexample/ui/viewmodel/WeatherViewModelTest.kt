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
    private val uiStateObserver = mock<Observer<WeatherViewModelUIState>>()
    private val navigationObserver = mock<Observer<WeatherViewModelNavigationTarget>>()

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        weatherViewModel = WeatherViewModel(useCase)
        weatherViewModel.uiState.observeForever(uiStateObserver)
        weatherViewModel.navigation.observeForever(navigationObserver)
    }

    @Test
    fun `verify successful response shows WeatherLoaded ui state`() =
        mainCoroutineRule.runBlockingTest {
            whenever(useCase.execute()).thenReturn(Result.OnSuccess(weatherModel))
            weatherViewModel.dispatch(WeatherViewModelEvent.LoadWeather)
            verify(uiStateObserver).onChanged(WeatherViewModelUIState.ShowLoading)
            verify(uiStateObserver).onChanged(WeatherViewModelUIState.WeatherLoaded(weatherModel))
        }

    @Test
    fun `verify unsuccessful response shows Error state`() = mainCoroutineRule.runBlockingTest {
        whenever(useCase.execute()).thenReturn(Result.OnError)
        weatherViewModel.dispatch(WeatherViewModelEvent.LoadWeather)
        verify(uiStateObserver).onChanged(WeatherViewModelUIState.ShowLoading)
        verify(uiStateObserver).onChanged(WeatherViewModelUIState.ShowError)
    }

    @Test
    fun `verify detail click event navigates to Detail screen` ()= mainCoroutineRule.runBlockingTest {
        weatherViewModel.dispatch(WeatherViewModelEvent.DetailButtonClick(weatherModel))
        verify(navigationObserver).onChanged(WeatherViewModelNavigationTarget.WeatherDetail(weatherModel))
    }

    private val weatherModel = WeatherModel(
        name = "Amsterdam",
        temperature = 23.0,
        maxTemperature = 30.0,
        minTemperature = 15.0
    )

}
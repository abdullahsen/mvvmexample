package com.asen.mvvmexample.ui.viewmodel

import com.asen.mvvmexample.model.WeatherModel

sealed class WeatherViewModelUIState{
    data class WeatherLoaded(val weather: WeatherModel): WeatherViewModelUIState()
    object ShowLoading: WeatherViewModelUIState()
    object ShowError: WeatherViewModelUIState()
}

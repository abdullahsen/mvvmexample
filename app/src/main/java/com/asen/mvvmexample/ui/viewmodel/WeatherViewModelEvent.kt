package com.asen.mvvmexample.ui.viewmodel

import com.asen.mvvmexample.model.WeatherModel

sealed class WeatherViewModelEvent {
    object LoadWeather : WeatherViewModelEvent()
    data class DetailButtonClick(val weatherModel: WeatherModel) : WeatherViewModelEvent()
}

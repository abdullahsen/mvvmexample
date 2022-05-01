package com.asen.mvvmexample.ui.viewmodel

import com.asen.mvvmexample.model.WeatherModel

sealed class WeatherViewModelNavigationTarget {
    data class WeatherDetail(val weatherModel: WeatherModel) : WeatherViewModelNavigationTarget()
}

package com.asen.mvvmexample.util

import com.asen.mvvmexample.model.WeatherModel

sealed class Result {
    data class OnSuccess(val weather: WeatherModel): Result()
    object OnError: Result()
}
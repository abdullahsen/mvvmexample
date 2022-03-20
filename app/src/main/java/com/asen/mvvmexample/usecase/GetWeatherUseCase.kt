package com.asen.mvvmexample.usecase

import com.asen.mvvmexample.util.Result

interface GetWeatherUseCase {
    suspend fun execute(): Result
}
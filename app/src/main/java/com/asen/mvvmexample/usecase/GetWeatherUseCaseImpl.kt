package com.asen.mvvmexample.usecase

import com.asen.mvvmexample.service.repository.WeatherRepository
import com.asen.mvvmexample.usecase.mapper.WeatherMapper
import com.asen.mvvmexample.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetWeatherUseCaseImpl(
    private val repository: WeatherRepository,
    private val mapper: WeatherMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GetWeatherUseCase {
    override suspend fun execute(): Result = withContext(ioDispatcher) {
        return@withContext try {
            val response = mapper.map(repository.getWeatherData())
            Result.OnSuccess(response)
        } catch (e: Exception) {
            Result.OnError
        }
    }
}
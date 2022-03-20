package com.asen.mvvmexample.usecase

import com.asen.mvvmexample.service.repository.WeatherRepository
import com.asen.mvvmexample.usecase.mapper.Mapper
import com.asen.mvvmexample.util.Result
import kotlinx.coroutines.Dispatchers import kotlinx.coroutines.withContext

class GetWeatherUseCaseImpl(private val repository: WeatherRepository, private val mapper: Mapper) :
    GetWeatherUseCase {
    override suspend fun execute(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = mapper.map(repository.getWeatherData())
            Result.OnSuccess(response)
        } catch (e: Exception) {
            Result.OnError
        }
    }
}
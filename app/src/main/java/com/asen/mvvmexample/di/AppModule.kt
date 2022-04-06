package com.asen.mvvmexample.di

import com.asen.mvvmexample.BuildConfig
import com.asen.mvvmexample.service.AuthInterceptor
import com.asen.mvvmexample.service.api.WeatherService
import com.asen.mvvmexample.service.repository.WeatherRepository
import com.asen.mvvmexample.service.repository.WeatherRepositoryImpl
import com.asen.mvvmexample.ui.viewmodel.WeatherViewModel
import com.asen.mvvmexample.usecase.GetWeatherUseCase
import com.asen.mvvmexample.usecase.GetWeatherUseCaseImpl
import com.asen.mvvmexample.usecase.mapper.WeatherMapper
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit


val appModules = module {
    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get()) }
    factory { provideWeatherApi(get()) }
    single { provideRetrofit(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single { WeatherMapper() }
    single<GetWeatherUseCase> { GetWeatherUseCaseImpl(get(), get()) }
    viewModel { WeatherViewModel(get()) }
}

val contentType: MediaType = MediaType.get("application/json")


private val json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    isLenient = true
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory(contentType)
        ).build()
}

private fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
}

private fun provideWeatherApi(retrofit: Retrofit): WeatherService =
    retrofit.create(WeatherService::class.java)

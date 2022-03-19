package com.asen.mvvmexample.di

import com.asen.mvvmexample.BuildConfig
import com.asen.mvvmexample.service.AuthInterceptor
import com.asen.mvvmexample.service.api.WeatherService
import com.asen.mvvmexample.service.repository.WeatherRepository
import com.asen.mvvmexample.ui.viewmodel.WeatherViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit


val appModules = module {
    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get()) }
    factory { provideCatFactApi(get()) }
    single { provideRetrofit(get()) }
    single { WeatherRepository(get()) }
    viewModel { WeatherViewModel(get()) }
}

val contentType: MediaType = MediaType.get("application/json")

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
        .addConverterFactory(
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
                isLenient = true
            }.asConverterFactory(MediaType.get("application/json"))
        ).build()
}

fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
}

fun provideCatFactApi(retrofit: Retrofit): WeatherService =
    retrofit.create(WeatherService::class.java)

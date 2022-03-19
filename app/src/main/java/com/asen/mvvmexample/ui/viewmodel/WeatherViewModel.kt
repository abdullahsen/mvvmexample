package com.asen.mvvmexample.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asen.mvvmexample.service.repository.WeatherRepository
import com.asen.mvvmexample.service.response.WeatherForecast
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository): ViewModel() {

    private val _weatherForecast = MutableLiveData<WeatherForecast>()
    val weatherForecast: LiveData<WeatherForecast>
        get() = _weatherForecast

    fun loadWeather(){
        viewModelScope.launch {
            try {
                _weatherForecast.value = repository.getWeatherData()
            } catch (e: Exception) {
                // Retrofit error
                e.printStackTrace()
            }
        }
    }


}
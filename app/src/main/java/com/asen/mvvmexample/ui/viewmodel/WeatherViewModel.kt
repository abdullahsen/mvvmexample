package com.asen.mvvmexample.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asen.mvvmexample.usecase.GetWeatherUseCase
import com.asen.mvvmexample.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WeatherViewModel(private val useCase: GetWeatherUseCase) : ViewModel() {

    private val _uiState = MutableLiveData<WeatherViewModelUIState>()
    val uiState: LiveData<WeatherViewModelUIState>
        get() = _uiState

    init {
        loadWeather()
    }

    private fun loadWeather() = viewModelScope.launch {
        _uiState.value = WeatherViewModelUIState.ShowLoading
        delay(4000)
        when (val response = useCase.execute()) {
            is Result.OnError -> _uiState.value = WeatherViewModelUIState.ShowError
            is Result.OnSuccess -> _uiState.value =
                WeatherViewModelUIState.WeatherLoaded(response.weather)
        }
    }

}
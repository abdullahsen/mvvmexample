package com.asen.mvvmexample.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asen.mvvmexample.model.WeatherModel
import com.asen.mvvmexample.usecase.GetWeatherUseCase
import com.asen.mvvmexample.util.Result
import com.asen.mvvmexample.util.SingleLiveEvent
import kotlinx.coroutines.launch

class WeatherViewModel(private val useCase: GetWeatherUseCase) : ViewModel() {

    private val _uiState = MutableLiveData<WeatherViewModelUIState>()
    val uiState: LiveData<WeatherViewModelUIState>
        get() = _uiState

    private val _navigation = SingleLiveEvent<WeatherViewModelNavigationTarget>()
    val navigation: LiveData<WeatherViewModelNavigationTarget>
        get() = _navigation

    fun dispatch(event: WeatherViewModelEvent) {
        when (event) {
            is WeatherViewModelEvent.DetailButtonClick -> navigateToDetail(event.weatherModel)
            is WeatherViewModelEvent.LoadWeather -> loadWeather()
        }
    }

    private fun navigateToDetail(weatherModel: WeatherModel) {
        _navigation.value = WeatherViewModelNavigationTarget.WeatherDetail(weatherModel)
    }

    private fun loadWeather() = viewModelScope.launch {
        _uiState.value = WeatherViewModelUIState.ShowLoading
        when (val response = useCase.execute()) {
            is Result.OnError -> _uiState.value = WeatherViewModelUIState.ShowError
            is Result.OnSuccess -> _uiState.value =
                WeatherViewModelUIState.WeatherLoaded(response.weather)
        }
    }

}
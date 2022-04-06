package com.asen.mvvmexample.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asen.mvvmexample.databinding.ActivityMainBinding
import com.asen.mvvmexample.ui.viewmodel.WeatherViewModel
import com.asen.mvvmexample.ui.viewmodel.WeatherViewModelUIState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: WeatherViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()
        viewModel.loadWeather()
    }

    private fun initObservers() {
        viewModel.uiState.observe(this) { renderUIState(it) }
    }

    private fun renderUIState(uiState: WeatherViewModelUIState) = when (uiState) {
        is WeatherViewModelUIState.ShowError -> binding.textView.text = "ERROR"
        is WeatherViewModelUIState.ShowLoading -> binding.textView.text = "LOADING"
        is WeatherViewModelUIState.WeatherLoaded -> binding.textView.text = uiState.weather.name
    }
}
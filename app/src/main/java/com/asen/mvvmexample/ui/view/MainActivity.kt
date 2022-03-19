package com.asen.mvvmexample.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asen.mvvmexample.databinding.ActivityMainBinding
import com.asen.mvvmexample.ui.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: WeatherViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadWeather()
        viewModel.weatherForecast.observe(this) { weatherForecast ->
            binding.textView.text = weatherForecast.weather[0].description
        }
    }
}
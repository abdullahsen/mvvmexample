package com.asen.mvvmexample.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.asen.mvvmexample.ui.viewmodel.WeatherViewModel
import com.asen.mvvmexample.databinding.FragmentWeatherBinding
import com.asen.mvvmexample.model.WeatherModel
import com.asen.mvvmexample.ui.viewmodel.WeatherViewModelEvent
import com.asen.mvvmexample.ui.viewmodel.WeatherViewModelNavigationTarget
import com.asen.mvvmexample.ui.viewmodel.WeatherViewModelUIState
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val weatherViewModel: WeatherViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel.dispatch(WeatherViewModelEvent.LoadWeather)
        initObservers()
    }

    private fun initObservers() {
        weatherViewModel.uiState.observe(viewLifecycleOwner) { renderUIState(it) }
        weatherViewModel.navigation.observe(viewLifecycleOwner) { handleNavigation(it) }
    }

    private fun handleNavigation(target: WeatherViewModelNavigationTarget) {
        binding.root.findNavController().navigate(WeatherFragmentDirections.actionWeatherFragmentToDetailFragment())
    }

    private fun renderUIState(uiState: WeatherViewModelUIState) = when (uiState) {
        is WeatherViewModelUIState.ShowError -> binding.cityNameText.text = "ERROR"
        is WeatherViewModelUIState.ShowLoading -> binding.cityNameText.text = "LOADING"
        is WeatherViewModelUIState.WeatherLoaded -> renderLoadedState(uiState.weather)
    }

    private fun renderLoadedState(weatherModel: WeatherModel){
        binding.cityNameText.text = weatherModel.name
        binding.detailButton.visibility = View.VISIBLE
        binding.detailButton.setOnClickListener {
            weatherViewModel.dispatch(WeatherViewModelEvent.DetailButtonClick(weatherModel))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.haystackreviews.nukingwinds.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.haystackreviews.nukingwinds.R
import com.haystackreviews.nukingwinds.databinding.WeatherFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()
    private var _binding: WeatherFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.weatherContent.getWeatherButton.setOnClickListener {
            updateWeatherFromEditText()
        }

        binding.weatherError.reload.setOnClickListener {
            updateWeatherFromDefaults()
        }
    }

    private fun updateWeatherFromEditText() {
        val lat = binding.weatherContent.latField.editText?.text.toString() ?: ""
        val lon = binding.weatherContent.lonField.editText?.text.toString() ?: ""
        viewModel.updateWeather(lat, lon)
    }

    private fun updateWeatherFromDefaults() {
        val lat = resources.getString(R.string.default_lat)
        val lon = resources.getString(R.string.default_lat)
        viewModel.updateWeather(lat, lon)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateWeatherFromEditText()

        viewModel.weatherState.observe(viewLifecycleOwner) { weatherState ->
            binding.viewFlipper.displayedChild = stateToFlipper(weatherState)
            when (weatherState) {
                Loading -> {
                }
                is Content -> {
                    binding.weatherContent.latField.editText?.setText(weatherState.lat)
                    binding.weatherContent.lonField.editText?.setText(weatherState.lon)
                    binding.weatherContent.windSpeed.text = weatherState.windSpeed
                    binding.weatherContent.windGust.text = weatherState.windGust
                    binding.weatherContent.alertDescription.text = weatherState.alertDescription
                }
                is Error -> {
                    binding.weatherError.errorMessage.text = weatherState.message
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun stateToFlipper(state: WeatherState): Int {
        return when (state) {
            Loading -> 0
            is Content -> 1
            is Error -> 2
        }
    }
}
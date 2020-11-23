package com.haystackreviews.nukingwinds.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.haystackreviews.nukingwinds.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.weatherState.observe(viewLifecycleOwner) { weatherState ->
            binding.viewFlipper.displayedChild = stateToFlipper(weatherState)
            when (weatherState) {
                Loading -> {
                }
                is Content -> {
                    binding.windSpeed.text = weatherState.windSpeed
                    binding.windGust.text = weatherState.windGust
                    binding.alertDescription.text = weatherState.alertDescription
                }
                is Error -> {
                    binding.errorMessage.text = weatherState.message
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
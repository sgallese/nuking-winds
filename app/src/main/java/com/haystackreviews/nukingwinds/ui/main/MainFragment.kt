package com.haystackreviews.nukingwinds.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haystackreviews.nukingwinds.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            Factory(requireActivity().application)
        ).get(MainViewModel::class.java)

        viewModel.oneCallState.observe(this) { oneCallState ->
            binding.viewFlipper.displayedChild = stateToFlipper(oneCallState)
            when (oneCallState) {
                Loading -> {
                }
                is Content -> {
                    binding.windSpeed.text = oneCallState.windSpeed
                    binding.windGust.text = oneCallState.windGust
                    binding.alertDescription.text = oneCallState.alertDescription
                }
                is Error -> {
                    binding.errorMessage.text = oneCallState.message
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

fun stateToFlipper(state: OneCallState): Int {
    return when (state) {
        Loading -> 0
        is Content -> 1
        is Error -> 2
    }
}
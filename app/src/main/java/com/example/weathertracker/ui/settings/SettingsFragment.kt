package com.example.weathertracker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weathertracker.databinding.FragmentSettingsBinding
import com.example.weathertracker.util.SettingsHelper

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var settingsHelper: SettingsHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsHelper = SettingsHelper.getInstance()

        settingsViewModel = SettingsViewModel(settingsHelper)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = settingsViewModel
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
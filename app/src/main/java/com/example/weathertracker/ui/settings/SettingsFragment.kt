package com.example.weathertracker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weathertracker.data.DbQueryHelper
import com.example.weathertracker.databinding.FragmentSettingsBinding
import com.example.weathertracker.util.SettingsHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var settingsHelper: SettingsHelper
    private lateinit var dbHelper: DbQueryHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsHelper = SettingsHelper.getInstance()
        dbHelper = DbQueryHelper(this.requireContext())

        settingsViewModel = SettingsViewModel(settingsHelper)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = settingsViewModel
        }

        binding.deleteDatabaseLinearLayout.setOnClickListener {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle("Delete Database?")
                .setMessage("Are you sure you wish to delete all weather data? This cannot be undone.")
                .setNegativeButton("CANCEL") { _, _ -> }
                .setPositiveButton("OK") {_, _ ->
                    dbHelper.deleteAllRecordsFromDatabase()
                }
                .show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.weathertracker.ui.settings

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.weathertracker.util.SettingsHelper

class SettingsViewModel(private val settingsHelper: SettingsHelper) : ViewModel() {

    val isSaveCheckpointToDatabaseEnabled =
        ObservableField<Boolean>(settingsHelper.getSaveCheckpointToDatabase())

    fun onSaveCheckpointToDatabaseClicked() {
        settingsHelper.setSaveCheckpointToDatabase(
            !(settingsHelper.getSaveCheckpointToDatabase())
        )
        isSaveCheckpointToDatabaseEnabled.set(
            settingsHelper.getSaveCheckpointToDatabase()
        )
    }
}
package com.example.weathertracker.util

class SettingsHelper {

    private var saveCheckpointToDatabase = true

    fun getSaveCheckpointToDatabase(): Boolean = saveCheckpointToDatabase

    fun setSaveCheckpointToDatabase(bool: Boolean) {
        saveCheckpointToDatabase = bool
    }

    companion object {
        private var settingsHelperInstance: SettingsHelper? = null

        fun getInstance(): SettingsHelper {
            return if(settingsHelperInstance == null) {
                settingsHelperInstance = SettingsHelper()
                settingsHelperInstance!!
            } else settingsHelperInstance!!
        }
    }
}
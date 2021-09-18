package com.example.weathertracker.data

import android.provider.BaseColumns

object CheckpointContract {
    object CheckpointEntry : BaseColumns {
        const val TABLE_NAME = "checkpoint"
        const val COLUMN_NAME_LAT = "latitude"
        const val COLUMN_NAME_LONG = "longitude"
        const val COLUMN_NAME_ADDRESS = "address"
        const val COLUMN_NAME_TEMP = "temperature"
        const val COLUMN_NAME_WEATHER_DESC = "weather_description"
    }
}
package com.example.weathertracker.data

import android.content.ContentValues
import android.content.Context
import android.location.Location
import com.example.weathertracker.data.CheckpointContract.CheckpointEntry
import com.google.android.gms.maps.model.LatLng

class DbQueryHelper(context: Context) {
    private var dbHelper: CheckpointDbHelper = CheckpointDbHelper(context)

    fun addNewCheckpoint(location: Location, address:String, weather: Pair<Double, String>): Long? {
        val db = dbHelper.writableDatabase
        val lat = location.latitude
        val long = location.longitude

        val values = ContentValues().apply {
            put(CheckpointEntry.COLUMN_NAME_LAT, lat)
            put(CheckpointEntry.COLUMN_NAME_LONG, long)
            put(CheckpointEntry.COLUMN_NAME_ADDRESS, address)
            put(CheckpointEntry.COLUMN_NAME_TEMP, weather.first - 273)
            put(CheckpointEntry.COLUMN_NAME_WEATHER_DESC, weather.second)
        }

        return db?.insert(CheckpointEntry.TABLE_NAME, null, values)
    }

    fun deleteCheckpoint(lat: Double, lng: Double) {
        val db = dbHelper.writableDatabase
        val selection =
            "${CheckpointEntry.COLUMN_NAME_LAT} = $lat AND ${CheckpointEntry.COLUMN_NAME_LONG} = $lng"
        db.delete(CheckpointEntry.TABLE_NAME, selection, null)
    }

    fun fetchAllCheckpoints(): Map<LatLng, List<String>>  {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            CheckpointEntry.COLUMN_NAME_LAT,
            CheckpointEntry.COLUMN_NAME_LONG,
            CheckpointEntry.COLUMN_NAME_ADDRESS,
            CheckpointEntry.COLUMN_NAME_TEMP,
            CheckpointEntry.COLUMN_NAME_WEATHER_DESC
        )
        val cursor = db.query(
            CheckpointEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        val locations = mutableMapOf<LatLng, List<String>>()
        with(cursor) {
            while (moveToNext()) {
                val lat = getDouble(getColumnIndexOrThrow(CheckpointEntry.COLUMN_NAME_LAT))
                val long = getDouble(getColumnIndexOrThrow(CheckpointEntry.COLUMN_NAME_LONG))
                val address = getString(getColumnIndexOrThrow(CheckpointEntry.COLUMN_NAME_ADDRESS))
                val weatherDesc =
                    getString(getColumnIndexOrThrow(CheckpointEntry.COLUMN_NAME_WEATHER_DESC))
                val temp =
                    getDouble(getColumnIndexOrThrow(CheckpointEntry.COLUMN_NAME_TEMP)).toString()
                locations.putIfAbsent(LatLng(lat, long), listOf<String>(address, temp, weatherDesc))
            }
        }
        cursor.close()
        return locations
    }
}
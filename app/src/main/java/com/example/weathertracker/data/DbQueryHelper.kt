package com.example.weathertracker.data

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.example.weathertracker.data.CheckpointContract.CheckpointEntry
import com.google.android.gms.maps.model.LatLng

class DbQueryHelper(context: Context) {
    private var dbHelper: CheckpointDbHelper = CheckpointDbHelper(context)

    fun addNewCheckpoint(checkpoint: CheckpointModel): Long? {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(CheckpointEntry.COLUMN_NAME_LAT, checkpoint.getLat())
            put(CheckpointEntry.COLUMN_NAME_LONG, checkpoint.getLng())
            put(CheckpointEntry.COLUMN_NAME_ADDRESS, checkpoint.getAddress())
            put(CheckpointEntry.COLUMN_NAME_TEMP, checkpoint.getTemp())
            put(CheckpointEntry.COLUMN_NAME_WEATHER_DESC, checkpoint.getWeatherDesc())
        }

        return db?.insert(CheckpointEntry.TABLE_NAME, null, values)
    }

    fun deleteCheckpoint(id: String) {
        val db = dbHelper.writableDatabase
        val selection = "${BaseColumns._ID} = $id"
        db.delete(CheckpointEntry.TABLE_NAME, selection, null)
    }

    fun fetchAllCheckpoints(): List<CheckpointModel> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
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
        val checkpoints = mutableListOf<CheckpointModel>()
        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(BaseColumns._ID))
                val lat = getDouble(getColumnIndexOrThrow(CheckpointEntry.COLUMN_NAME_LAT))
                val lng = getDouble(getColumnIndexOrThrow(CheckpointEntry.COLUMN_NAME_LONG))
                val address = getString(getColumnIndexOrThrow(CheckpointEntry.COLUMN_NAME_ADDRESS))
                val weatherDesc =
                    getString(getColumnIndexOrThrow(CheckpointEntry.COLUMN_NAME_WEATHER_DESC))
                val temp = getDouble(getColumnIndexOrThrow(CheckpointEntry.COLUMN_NAME_TEMP))

                val checkpoint = CheckpointModel()
                checkpoint.setID(id)
                checkpoint.setLat(lat)
                checkpoint.setLng(lng)
                checkpoint.setAddress(address)
                checkpoint.setTemp(temp)
                checkpoint.setWeatherDesc(weatherDesc)

                checkpoints.add(checkpoint)
            }
        }
        cursor.close()
        return checkpoints
    }

    fun checkIfCheckpointIsSaved(lat: Double, lng: Double): Boolean {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            CheckpointEntry.COLUMN_NAME_LAT,
            CheckpointEntry.COLUMN_NAME_LONG,
        )
        val selection =
            "${CheckpointEntry.COLUMN_NAME_LAT} = $lat AND ${CheckpointEntry.COLUMN_NAME_LONG} = $lng"
        val cursor = db.query(
            CheckpointEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        var res = false
        res = cursor.count > 0
        cursor.close()
        return res
    }
}
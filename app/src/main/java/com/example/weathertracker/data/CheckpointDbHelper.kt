package com.example.weathertracker.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.weathertracker.data.CheckpointContract.CheckpointEntry

class CheckpointDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_NAME = "Checkpoint.db"
        const val DATABASE_VERSION = 1

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${CheckpointEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${CheckpointEntry.COLUMN_NAME_LAT} INTEGER," +
                    "${CheckpointEntry.COLUMN_NAME_LONG} INTEGER," +
                    "${CheckpointEntry.COLUMN_NAME_ADDRESS} TEXT," +
                    "${CheckpointEntry.COLUMN_NAME_TEMP} REAL," +
                    "${CheckpointEntry.COLUMN_NAME_WEATHER_DESC} TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CheckpointEntry.TABLE_NAME}"

    }

}
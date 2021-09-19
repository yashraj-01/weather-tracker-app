package com.example.weathertracker.data

class CheckpointModel {
    private lateinit var id: String
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private lateinit var address: String
    private var temp: Double = 0.0
    private lateinit var weatherDesc: String

    fun getID(): String = id
    fun getLat(): Double = lat
    fun getLng(): Double = lng
    fun getAddress(): String = address
    fun getTemp(): Double = temp
    fun getWeatherDesc(): String = weatherDesc

    fun setID(id: String) { this.id = id }
    fun setLat(lat: Double) { this.lat = lat }
    fun setLng(lng: Double) { this.lng = lng }
    fun setAddress(address: String) { this.address = address }
    fun setTemp(temp: Double) { this.temp = temp }
    fun setWeatherDesc(weatherDesc: String) { this.weatherDesc = weatherDesc }
}
package com.example.weathertracker.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weathertracker.MainActivity
import com.example.weathertracker.R
import com.example.weathertracker.data.CheckpointContract.CheckpointEntry
import com.example.weathertracker.data.CheckpointDbHelper
import com.example.weathertracker.databinding.FragmentMapBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.util.*
import kotlin.math.round

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapViewModel: MapViewModel
    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var dbHelper: CheckpointDbHelper
    private lateinit var gMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private lateinit var mLocationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)

        dbHelper = CheckpointDbHelper(this.requireContext())

        val mapFragment: SupportMapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity as MainActivity)

        geocoder = Geocoder(this.requireContext(), Locale.getDefault())

        val requestPermissionLauncher =
            registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    requestCurrentLocation()
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(
                        activity,
                        "App needs location permission. Please grant access.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        binding.currLocFab.setOnClickListener {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    activity as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    // You can use the API that requires the permission.
                    requestCurrentLocation()
                }
                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
            }
        }

        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation() {
        val mLocationRequest = LocationRequest.create().apply {
            interval = 5
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime= 100
        }
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }

    private fun getAddress(lat: Double, long: Double): String {
        val addressList: List<Address> = geocoder.getFromLocation(lat, long, 1)
        var address = ""
        if (addressList.isNotEmpty()) {
            val locality = addressList[0].subLocality
            val city = addressList[0].locality
            address = "$locality, $city"
        }
        return address
    }

    private fun getWeather(location: Location) {
        val queue = Volley.newRequestQueue(this.requireContext())
        val apiKey = activity?.getString(R.string.open_weather_api_key)
        val lat = location.latitude
        val long = location.longitude
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$long&appid=$apiKey"
        var result: Pair<Double, String> = Pair(0.0, "")

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                result = getWeatherFromRequest(response)
                addCheckpointToDb(location, result)
            },
            {
                Toast.makeText(
                    activity,
                    "That didn't work!",
                    Toast.LENGTH_SHORT
                ).show()
            })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun getWeatherFromRequest(s: String): Pair<Double, String> {
        val jsonObject = JSONObject(s)
        var temp = jsonObject.getJSONObject("main").getDouble("temp")
        temp = round(temp * 100) / 100
        val weatherDesc = (jsonObject.getJSONArray("weather")[0] as JSONObject).getString("description")
        return Pair(temp, weatherDesc)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addCheckpointToDb(location: Location, weather: Pair<Double, String>) {
        val db = dbHelper.writableDatabase
        val lat = location.latitude
        val long = location.longitude
        val address = getAddress(lat, long)

        val values = ContentValues().apply {
            put(CheckpointEntry.COLUMN_NAME_LAT, lat)
            put(CheckpointEntry.COLUMN_NAME_LONG, long)
            put(CheckpointEntry.COLUMN_NAME_ADDRESS, address)
            put(CheckpointEntry.COLUMN_NAME_TEMP, weather.first - 273)
            put(CheckpointEntry.COLUMN_NAME_WEATHER_DESC, weather.second)
        }

        val newRowId = db?.insert(CheckpointEntry.TABLE_NAME, null, values)
        if(newRowId == -1L) {
            Toast.makeText(
                activity,
                "Something went wrong. Checkpoint not added to database.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                activity,
                "Lat/Lng: (${values[CheckpointEntry.COLUMN_NAME_LAT]}, ${values[CheckpointEntry.COLUMN_NAME_LONG]})\n" +
                "You were here: ${values[CheckpointEntry.COLUMN_NAME_ADDRESS]}" +
                "Temp: ${"%.2f".format(values[CheckpointEntry.COLUMN_NAME_TEMP])}\u2103 (${values[CheckpointEntry.COLUMN_NAME_WEATHER_DESC]})",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                gMap.clear()
                val locationList = locationResult.locations
                if (locationList.isNotEmpty()) {
                    val location = locationList.last()
                    getWeather(location)
                    val lat = location.latitude
                    val long = location.longitude
                    val latLang = LatLng(lat, long)
                    gMap.addMarker(
                        MarkerOptions().position(latLang).title("$lat, $long")
                    )
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, 15F))
                }
                fusedLocationClient.removeLocationUpdates(mLocationCallback)
            }
        }
    }
}
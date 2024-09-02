package com.example.weatherapp.view.weather

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherConditionsItem
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.example.weatherapp.util.ConnectivityManager
import com.example.weatherapp.util.Constants
import com.example.weatherapp.util.loadAndTintIcon
import com.example.weatherapp.view.base.BaseActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {

    @Inject
    lateinit var weatherAdapter: WeatherAdapter

    override val bindingInflater: (LayoutInflater) -> ActivityWeatherBinding
        get() = ActivityWeatherBinding::inflate

    private val weatherViewModel: WeatherViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private lateinit var iconCode: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
        checkIfLocationPermissionGranted()

        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )
    }

    private fun checkIfLocationPermissionGranted() {
        if (hasPermission()) {
            checkLocationSettings()
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

        }
    }

    private fun hasPermission(): Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION,
                false
            ) && permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                checkLocationSettings()
            }

            else -> {
                handlePermissionDenial()
            }
        }
        /*when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }

            else -> {
                // No location access granted.
            }
        }*/
    }

    private fun subscribeObservers() {
        weatherViewModel.weatherResponseLiveData.observe(this) { weatherConditions ->
            weatherConditions?.let {
                iconCode = it.icon.replace("n", "d")

                binding.tvCity.text = it.name
                binding.tvTemp.text = "${it.temp.toInt()}°"
                binding.tvWeatherDetails.text =
                    "${it.main} \n Min Temp / Max Temp : ${it.tempMin.toInt()} / ${it.tempMax.toInt()}"
                loadAndTintIcon(
                    this,
                    binding.ivIcon,
                    Constants.WEATHER_API_IMAGE_ENDPOINT + "${iconCode}@4x.png",
                    R.color.white
                )
                setUpAdapter(it.weatherConditionsItem)
                changeBackgroundColor(iconCode)
            }
        }

        weatherViewModel.isLoading.observe(this) {
            showLoader(it)
        }
    }

    private fun setUpAdapter(weatherConditionsItem: ArrayList<WeatherConditionsItem>) {
        weatherAdapter.addAll(weatherConditionsItem)
        binding.rvWeatherConditions.apply {
            layoutManager = GridLayoutManager(this@WeatherActivity, 3)
            adapter = weatherAdapter
        }
    }

    private fun changeBackgroundColor(iconCode: String?) {
        when (iconCode) {
            "01d", "02d", "03d" -> binding.weather.setBackgroundResource(R.drawable.sunny_gradient_drawable)
            "04d", "09d", "10d", "11d" -> binding.weather.setBackgroundResource(R.drawable.rainy_gradient_drawable)
            "13d", "50d" -> binding.weather.setBackgroundResource(R.drawable.snow_gradient_drawable)
        }
    }

    private fun showLoader(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun getCityName(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val cityName = addresses?.get(0)?.locality
        weatherViewModel.getCityWeather(cityName)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop location updates when the activity is destroyed
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    private fun handlePermissionDenial() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Without location permission, the app cannot function properly. You can enable it from app settings.")
            .setPositiveButton("App Settings") { dialog, _ ->
                dialog.dismiss()

                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()

                Toast.makeText(this, "Permission denied. Feature unavailable.", Toast.LENGTH_SHORT)
                    .show()
            }
            .show()
    }

    private fun createRequest(): LocationRequest {
        // New builder
        return LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            if (location != null) {
                getCityName(location.latitude, location.longitude)
            } else {
                Toast.makeText(
                    this@WeatherActivity,
                    "Unable to retrieve location",
                    Toast.LENGTH_LONG
                ).show()
            }
            // Stop location updates after getting the current location
            fusedLocationClient.removeLocationUpdates(this)
        }
    }

    private fun checkLocationSettings() {
        locationRequest = createRequest()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        result.addOnSuccessListener {
            getLocation()
        }

        result.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied. But could be fixed by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                    exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                } catch (classCastEx: ClassCastException) {
                    // Ignore, should be an impossible error.
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                RESULT_OK -> {
                    if (ConnectivityManager.isInternetAvailable(this)) {
                        getLocation()
                    } else {
                        Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
                    }
                }

                RESULT_CANCELED -> {
                    Toast.makeText(this, "Please give location permission.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }
    }

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1001
    }
}
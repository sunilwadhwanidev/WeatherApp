package com.snapwork.weatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.snapwork.weatherapp.BuildConfig
import com.snapwork.weatherapp.R
import com.snapwork.weatherapp.data.local.WeatherDatabase
import com.snapwork.weatherapp.data.local.WeatherEntity
import com.snapwork.weatherapp.data.repository.WeatherRepository
import com.snapwork.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.snapwork.weatherapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class CurrentWeatherFragment : Fragment() {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: WeatherRepository
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        val dao = WeatherDatabase.getInstance(requireContext()).weatherDao()
        repository = WeatherRepository(dao)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getCurrentLocation()

        return binding.root
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                fetchWeather(it.latitude, it.longitude)
            } ?: run {
                // fallback (Mumbai if location is null)
                fetchWeather(19.0760, 72.8777)
            }
        }
    }

    private fun fetchWeather(lat: Double, lon: Double) {
        val apiKey = BuildConfig.OPENWEATHER_API_KEY


        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getCurrentWeather(lat, lon, apiKey)
            }
            if (response.isSuccessful) {
                response.body()?.let { weather ->
                    val sunrise = Date(weather.sys.sunrise * 1000)
                    val sunset = Date(weather.sys.sunset * 1000)
                    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

                    binding.cityTv.text = "${weather.name}, ${weather.sys.country}"
                    binding.tempTv.text = "${weather.main.temp} °C"
                    binding.sunriseTv.text = "Sunrise: ${sdf.format(sunrise)}"
                    binding.sunsetTv.text = "Sunset: ${sdf.format(sunset)}"

                    // Icon selection
                    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    val condition = weather.weather.firstOrNull()?.main ?: "Clear"
                    val iconRes = when {
                        condition.contains("Rain", true) -> R.drawable.ic_rain
                        currentHour >= 18 -> R.drawable.ic_moon
                        else -> R.drawable.ic_sun
                    }
                    binding.weatherIcon.setImageResource(iconRes)

                    // Save in DB
                    val entity = WeatherEntity(
                        city = weather.name,
                        country = weather.sys.country ?: "",
                        temperature = weather.main.temp,
                        description = condition,
                        icon = condition,
                        timestamp = System.currentTimeMillis()
                    )
                    lifecycleScope.launch(Dispatchers.IO) {
                        repository.insertWeather(entity)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


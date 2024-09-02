package com.example.weatherapp.view.weather

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherConditions
import com.example.weatherapp.domain.ResultWrapper
import com.example.weatherapp.domain.usecase.WeatherUseCase
import com.example.weatherapp.util.ConnectivityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase, @ApplicationContext private val appContext: Context
) : ViewModel() {

    val weatherResponseLiveData = MutableLiveData<WeatherConditions?>()
    private val errorLiveData = MutableLiveData<Exception>()
    val isLoading = MutableLiveData<Boolean>()
    private val isInternetConnected = MutableLiveData<Boolean>()

    fun getCityWeather(city: String?) {
        isLoading.value = true
        viewModelScope.launch {
            if (ConnectivityManager.isInternetAvailable(appContext)) {
                weatherUseCase.getCityWeather(city).collect {
                    when (it) {
                        is ResultWrapper.Success -> {
                            isLoading.value = false
                            weatherResponseLiveData.value = it.data
                        }

                        is ResultWrapper.Error -> {
                            isLoading.value = false
                            errorLiveData.value = it.exception
                        }
                    }
                }
            } else {
                weatherUseCase.getCityWeatherFromDb().collect {
                    when (it) {
                        is ResultWrapper.Success -> {
                            isLoading.value = false
                            if (it.data != null) {
                                weatherResponseLiveData.value = it.data
                            } else {
                                isInternetConnected.value = false
                            }
                        }

                        is ResultWrapper.Error -> {
                            isLoading.value = false
                            errorLiveData.value = it.exception
                        }
                    }
                }

            }
        }
    }
}
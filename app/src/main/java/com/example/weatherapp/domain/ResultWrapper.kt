package com.example.weatherapp.domain

import java.lang.Exception

sealed class ResultWrapper<out T> {
    data class Error(var exception: Exception) : ResultWrapper<Nothing>()
    data class Success<T>(var data: T) : ResultWrapper<T>()
}
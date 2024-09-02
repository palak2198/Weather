package com.example.weatherapp.di.module

import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.data.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoriesModule {

    @Binds
    fun weatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}
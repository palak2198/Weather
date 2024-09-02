package com.example.weatherapp.di.module

import android.app.Application
import androidx.room.Room
import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.db.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): WeatherDatabase {
        return Room.databaseBuilder(
            app,
            WeatherDatabase::class.java,
            WeatherDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideWeatherDao(appDatabase: WeatherDatabase): WeatherDao {
        return appDatabase.weatherDao
    }
}
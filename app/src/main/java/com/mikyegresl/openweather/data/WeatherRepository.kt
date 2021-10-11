package com.mikyegresl.openweather.data

import androidx.lifecycle.LiveData
import com.mikyegresl.openweather.data.model.Weather
import com.mikyegresl.openweather.data.model.WeatherResult
import io.reactivex.Completable
import io.reactivex.Observable

interface WeatherRepository {
    fun fetchWeatherData(cityId: String): Observable<WeatherResult>

    fun observeWeatherData(cityId: String): LiveData<Weather>

    fun saveWeatherData(weather: Weather): Completable
}
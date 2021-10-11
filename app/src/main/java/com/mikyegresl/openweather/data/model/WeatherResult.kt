package com.mikyegresl.openweather.data.model

sealed class WeatherResult {
    data class Success(val weather: Weather): WeatherResult()
    object Failure: WeatherResult()
}

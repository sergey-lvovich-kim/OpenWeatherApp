package com.mikyegresl.openweather.data.model

sealed class CityResult {
    object Success: CityResult()
    object Failure: CityResult()
}
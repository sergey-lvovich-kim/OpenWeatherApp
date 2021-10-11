package com.mikyegresl.openweather.view.screens

import android.view.View
import androidx.navigation.findNavController
import com.mikyegresl.openweather.view.screens.fragments.WeatherFragmentDirections

class ScreensNavigator(
    private val view: View
) {
    fun toFavoriteCities() {
        view.findNavController().navigate(
            WeatherFragmentDirections.actionWeatherFragmentToFavoriteCitiesFragment()
        )
    }

    fun toSearchCity() {
        view.findNavController().navigate(
            WeatherFragmentDirections.actionWeatherFragmentToFavoriteCitiesFragment()
        )
    }

    fun toWeatherData() {
        view.findNavController().navigate(
            WeatherFragmentDirections.actionWeatherFragmentToFavoriteCitiesFragment()
        )
    }
}
package com.mikyegresl.openweather.view.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikyegresl.openweather.view.screens.containers.FavoriteCitiesViewContainer
import com.mikyegresl.openweather.view.screens.containers.SearchCityViewContainer
import com.mikyegresl.openweather.view.screens.containers.WeatherViewContainer
import javax.inject.Inject

class ViewContainerFactory @Inject constructor(
    private val layoutInflater: LayoutInflater
) {
    fun newWeatherViewContainer(parent: ViewGroup?): WeatherViewContainer =
        WeatherViewContainer(layoutInflater, parent)

    fun newFavoriteCitiesViewContainer(parent: ViewGroup?) {
        FavoriteCitiesViewContainer(layoutInflater, parent)
    }

    fun newSearchCitiesViewContainer(parent: ViewGroup?): SearchCityViewContainer =
        SearchCityViewContainer(layoutInflater, parent)
}
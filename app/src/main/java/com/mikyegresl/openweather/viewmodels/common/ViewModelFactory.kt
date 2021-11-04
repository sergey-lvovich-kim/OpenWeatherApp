package com.mikyegresl.openweather.viewmodels.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mikyegresl.openweather.data.local.Prefs
import com.mikyegresl.openweather.viewmodels.DisplayWeatherViewModel
import com.mikyegresl.openweather.viewmodels.SearchCityViewModel
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val weatherViewModelProvider: Provider<DisplayWeatherViewModel>,
    private val citiesViewModelProvider: Provider<SearchCityViewModel>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            DisplayWeatherViewModel::class.java -> weatherViewModelProvider.get()
            SearchCityViewModel::class.java -> citiesViewModelProvider.get()
            else -> throw IllegalArgumentException("unsupported ViewModel class $modelClass")
        } as T
    }
}
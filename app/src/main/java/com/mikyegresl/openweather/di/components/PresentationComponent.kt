package com.mikyegresl.openweather.di.components

import com.mikyegresl.openweather.di.modules.PresentationModule
import com.mikyegresl.openweather.di.scopes.PresentationScope
import com.mikyegresl.openweather.view.screens.MainActivity
import com.mikyegresl.openweather.view.screens.fragments.SavedCitiesFragment
import com.mikyegresl.openweather.view.screens.fragments.SearchCityFragment
import com.mikyegresl.openweather.view.screens.fragments.WeatherFragment
import dagger.Subcomponent

@PresentationScope
@Subcomponent(modules = [PresentationModule::class])
interface PresentationComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: WeatherFragment)
    fun inject(fragment: SearchCityFragment)
    fun inject(fragment: SavedCitiesFragment)
}
package com.mikyegresl.openweather.view.screens.containers

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikyegresl.openweather.R
import com.mikyegresl.openweather.view.common.BaseViewContainer

class FavoriteCitiesViewContainer(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?
): BaseViewContainer<FavoriteCitiesViewContainer.Listener>(
    layoutInflater,
    viewGroup,
    R.layout.fragment_favorite_cities
) {
    interface Listener {

    }
}
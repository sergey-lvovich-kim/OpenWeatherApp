package com.mikyegresl.openweather.view.screens.containers

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikyegresl.openweather.R
import com.mikyegresl.openweather.view.common.BaseViewContainer

class SearchCityViewContainer(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?
): BaseViewContainer<SearchCityViewContainer.Listener>(
    layoutInflater,
    viewGroup,
    R.layout.fragment_search_city
) {
    interface Listener {

    }
}
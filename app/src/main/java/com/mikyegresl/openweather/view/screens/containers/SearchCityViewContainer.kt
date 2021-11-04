package com.mikyegresl.openweather.view.screens.containers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikyegresl.openweather.R
import com.mikyegresl.openweather.data.model.City
import com.mikyegresl.openweather.view.adapters.CitiesAdapter
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
        fun onSearchCity(query: String?)
        fun onCityClicked(city: City)
    }

    private val context: Context = layoutInflater.context
    private val searchCityEditText: EditText = findViewById(R.id.search_city_edit_text)
    private val searchCityRecyclerView: RecyclerView = findViewById(R.id.searched_cities_recycler_view)
    private val citiesAdapter: CitiesAdapter

    init {
        searchCityEditText.addTextChangedListener {
            for (listener in listeners) {
                listener.onSearchCity(it?.trim().toString())
            }
        }
        citiesAdapter = CitiesAdapter(context) { clickedCity ->
            for (listener in listeners) {
                listener.onCityClicked(clickedCity)
            }
        }
        searchCityRecyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL

        searchCityRecyclerView.layoutManager = layoutManager
        searchCityRecyclerView.adapter = citiesAdapter
    }

    fun bindCities(cities: List<City>) {
        citiesAdapter.bindCityList(cities)
    }
}
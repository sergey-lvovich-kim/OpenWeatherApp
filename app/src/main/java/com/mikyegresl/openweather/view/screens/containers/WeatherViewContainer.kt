package com.mikyegresl.openweather.view.screens.containers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mikyegresl.openweather.R
import com.mikyegresl.openweather.data.model.Weather
import com.mikyegresl.openweather.view.common.BaseViewContainer

class WeatherViewContainer(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?
): BaseViewContainer<WeatherViewContainer.Listener>(
    layoutInflater,
    viewGroup,
    R.layout.fragment_weather
) {
    interface Listener {
        fun onRefreshClicked()
        fun onSearchClicked()
        fun onFavoritesClicked()
        fun onIsFavoriteClicked(isFavorite: Boolean)
    }
    private val context: Context = layoutInflater.context
    private val swiperRefresh: SwipeRefreshLayout = findViewById(R.id.swipe_refresh)
    private val cityNameTextView: TextView = findViewById(R.id.city_name_text_view)
    private val weatherDescriptionTextView: TextView = findViewById(R.id.description_text_view)
    private val temperatureTextView: TextView = findViewById(R.id.temperature_text_view)
    private val humidityTextView: TextView = findViewById(R.id.humidity_text_view)
    private val pressureTextView: TextView = findViewById(R.id.pressure_text_view)
    private val addToFavsCheckBox: CheckBox = findViewById(R.id.add_to_favs_checkbox)
    private val searchCityBtn: Button = findViewById(R.id.open_search_btn)
    private val openFavoritesBtn: Button = findViewById(R.id.favorite_cities_btn)

    init {
        swiperRefresh.setOnRefreshListener {
            for (listener in listeners) {
                listener.onRefreshClicked()
            }
        }

        addToFavsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            for (listener in listeners) {
                listener.onIsFavoriteClicked(isChecked)
            }
        }

        searchCityBtn.setOnClickListener {
            for (listener in listeners) {
                listener.onSearchClicked()
            }
        }

        openFavoritesBtn.setOnClickListener {
            for (listener in listeners) {
                listener.onFavoritesClicked()
            }
        }
    }

    fun bindCityName(cityName: String) {
        cityNameTextView.text = cityName
    }

    fun bindWeatherData(weather: Weather) {
        weatherDescriptionTextView.text = weather.description
        temperatureTextView.text = context.getString(R.string.temperature_degrees, weather.temperature.toString(), "\u00B0")
        humidityTextView.text = context.getString(R.string.humidity, weather.humidity.toString())
        pressureTextView.text = context.getString(R.string.pressure, weather.pressure.toString())
    }

    fun bindIsFavorite(isFavorite: Boolean) {
        addToFavsCheckBox.isChecked = isFavorite
    }

    fun showProgressIndication() {
        swiperRefresh.isRefreshing = true
    }

    fun hideProgressIndication() {
        if (swiperRefresh.isRefreshing) {
            swiperRefresh.isRefreshing = false
        }
    }
}
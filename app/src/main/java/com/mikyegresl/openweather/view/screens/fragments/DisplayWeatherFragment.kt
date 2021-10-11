package com.mikyegresl.openweather.view.screens.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mikyegresl.openweather.data.local.Database
import com.mikyegresl.openweather.data.local.Prefs
import com.mikyegresl.openweather.data.model.WeatherResult
import com.mikyegresl.openweather.view.screens.containers.WeatherViewContainer
import com.mikyegresl.openweather.view.common.BaseFragment
import com.mikyegresl.openweather.view.common.ViewContainerFactory
import com.mikyegresl.openweather.view.screens.dialogs.DialogsNavigator
import com.mikyegresl.openweather.viewmodels.DisplayWeatherViewModel
import com.mikyegresl.openweather.viewmodels.common.ViewModelFactory
import javax.inject.Inject

class WeatherFragment : BaseFragment(), WeatherViewContainer.Listener {
    @Inject lateinit var viewContainerFactory: ViewContainerFactory
    @Inject lateinit var dialogsNavigator: DialogsNavigator
    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var prefs: Prefs

    private lateinit var viewContainer: WeatherViewContainer

    private lateinit var viewModel: DisplayWeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presentationComponent.inject(this)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(DisplayWeatherViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        val citiesInserted = prefs.getBoolean(Prefs.CITIES_INSERTED)

        Log.i(DisplayWeatherViewModel::class.qualifiedName, "citiesInserted=$citiesInserted")

        viewModel.saveCities(citiesInserted,requireContext().assets.open(Database.CITIES_FILENAME))
        viewContainer.registerListener(this)
    }

    override fun onStop() {
        super.onStop()

        viewContainer.unregisterListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewContainer = viewContainerFactory.newWeatherViewContainer(container)
        return viewContainer.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataLoading.observe(viewLifecycleOwner) {
            if (it) {
                viewContainer.showProgressIndication()
            }
            else {
                viewContainer.hideProgressIndication()
            }
        }

        viewModel.citiesInserted.observe(viewLifecycleOwner) {
            prefs.putBoolean(Prefs.CITIES_INSERTED, it)
        }

        viewModel.weatherData.observe(viewLifecycleOwner) { weatherResult ->
            when (weatherResult) {
//                is WeatherResult.Success -> {
//                    viewContainer.bindWeatherData(
//                        weatherResult.weather.description,
//                        weatherResult.weather.temperature,
//                        weatherResult.weather.humidity,
//                        weatherResult.weather.pressure)
//                }
                is WeatherResult.Failure -> {
                    dialogsNavigator.showApiErrorDialog()
                }
            }
        }

        viewModel.currentLocation.observe(viewLifecycleOwner) {
        }

        viewModel.getCityData().observe(viewLifecycleOwner) {
            viewContainer.bindCityName(it.name)
            viewContainer.bindIsFavorite(it.isFavorite)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherFragment()
    }

    override fun onRefreshClicked() {
        viewModel.fetchWeatherData()
    }

    override fun onSearchClicked() {
        findNavController().navigate(
            WeatherFragmentDirections.actionWeatherFragmentToFavoriteCitiesFragment()
        )
    }

    override fun onFavoritesClicked() {
        findNavController().navigate(
            WeatherFragmentDirections.actionWeatherFragmentToSearchCityFragment()
        )
    }

    override fun onIsFavoriteClicked(isFavorite: Boolean) {
        if (isFavorite) {
            viewModel.addToFavorites()
            return
        }
        viewModel.removeFromFavorites()
    }

    private fun fetchWeatherData() {
        viewModel.fetchWeatherData()
    }
}
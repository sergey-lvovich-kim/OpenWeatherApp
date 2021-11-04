package com.mikyegresl.openweather.view.screens.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mikyegresl.openweather.data.local.Database
import com.mikyegresl.openweather.data.local.Prefs
import com.mikyegresl.openweather.data.model.Point
import com.mikyegresl.openweather.data.model.WeatherResult
import com.mikyegresl.openweather.view.screens.containers.WeatherViewContainer
import com.mikyegresl.openweather.view.common.BaseFragment
import com.mikyegresl.openweather.view.common.ViewContainerFactory
import com.mikyegresl.openweather.view.screens.dialogs.DialogsNavigator
import com.mikyegresl.openweather.viewmodels.DisplayWeatherViewModel
import com.mikyegresl.openweather.viewmodels.common.ViewModelFactory
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import javax.inject.Inject

class WeatherFragment : BaseFragment(), WeatherViewContainer.Listener {
    @Inject lateinit var viewContainerFactory: ViewContainerFactory
    @Inject lateinit var dialogsNavigator: DialogsNavigator
    @Inject lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewContainer: WeatherViewContainer

    private lateinit var viewModel: DisplayWeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presentationComponent.inject(this)

        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(DisplayWeatherViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        viewModel.saveCities(requireContext().assets.open(Database.CITIES_FILENAME))
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

        viewModel.dataLoading.observe(viewLifecycleOwner) { dataLoading ->
            if (dataLoading) {
                viewContainer.showProgressIndication()
            }
            else {
                viewContainer.hideProgressIndication()
            }
        }

        viewModel.fetchWeatherResult.observe(viewLifecycleOwner) { weatherResult ->
            when (weatherResult) {
                is WeatherResult.Success -> {
                    viewContainer.bindWeatherData(weatherResult.weather)
                }
                is WeatherResult.Failure -> {
                    dialogsNavigator.showApiErrorDialog()
                }
            }
        }

        viewModel.currentCity.observe(viewLifecycleOwner) { city ->
            viewContainer.bindCityName(city.name)
            viewContainer.bindIsFavorite(city.isFavorite)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherFragment()

        private const val TAG = "DisplayWeatherFragment"
    }

    override fun onRefreshClicked() {
        viewModel.refreshWeatherData()
    }

    override fun onSearchClicked() {
        findNavController().navigate(
            WeatherFragmentDirections.actionWeatherFragmentToSearchCityFragment()
        )
    }

    override fun onFavoritesClicked() {
        findNavController().navigate(
            WeatherFragmentDirections.actionWeatherFragmentToFavoriteCitiesFragment()
        )
    }

    override fun onIsFavoriteClicked(isFavorite: Boolean) {
        if (isFavorite) {
            viewModel.addToFavorites()
            return
        }
        viewModel.removeFromFavorites()
    }
}
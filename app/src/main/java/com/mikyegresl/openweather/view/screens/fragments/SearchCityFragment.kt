package com.mikyegresl.openweather.view.screens.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mikyegresl.openweather.R
import com.mikyegresl.openweather.data.model.City
import com.mikyegresl.openweather.view.common.BaseFragment
import com.mikyegresl.openweather.view.common.ViewContainerFactory
import com.mikyegresl.openweather.view.screens.containers.SearchCityViewContainer
import com.mikyegresl.openweather.view.screens.dialogs.DialogsNavigator
import com.mikyegresl.openweather.viewmodels.SearchCityViewModel
import com.mikyegresl.openweather.viewmodels.common.ViewModelFactory
import javax.inject.Inject

class SearchCityFragment : BaseFragment(), SearchCityViewContainer.Listener {
    @Inject lateinit var viewContainerFactory: ViewContainerFactory
    @Inject lateinit var dialogsNavigator: DialogsNavigator
    @Inject lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewContainer: SearchCityViewContainer

    private lateinit var viewModel: SearchCityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presentationComponent.inject(this)

        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(SearchCityViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        viewContainer.registerListener(this)
    }

    override fun onStop() {
        super.onStop()

        viewContainer.unregisterListener(this)
        viewModel.stop()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewContainer = viewContainerFactory.newSearchCitiesViewContainer(container)
        return viewContainer.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel._currentCityList.observe(viewLifecycleOwner) { cityList ->
            Log.i(TAG, "searchCities: $cityList")
            viewContainer.bindCities(cityList)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchCityFragment()
    }

    override fun onSearchCity(query: String?) {
        if (query == null) {
            return
        }
        if (query.length >= 3) {
            Log.i(TAG, "searchCity: $query")
            viewModel.searchCity(query)
        }
    }

    override fun onCityClicked(city: City) {
        findNavController().navigate(
            SearchCityFragmentDirections.actionSearchCityFragmentToWeatherFragment()
        )
    }
}

private const val TAG = "SearchCityFragment"
package com.mikyegresl.openweather.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.mikyegresl.openweather.data.CitiesRepository
import com.mikyegresl.openweather.data.local.Database
import com.mikyegresl.openweather.data.model.City
import com.mikyegresl.openweather.data.model.CityResult
import io.reactivex.disposables.CompositeDisposable
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

class SearchCityViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository
): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _dataLoading = MutableLiveData<Boolean>()
    private val _searchCityName = MutableLiveData<String>()
    val _currentCityList = MediatorLiveData<List<City>>()

    val dataLoading: LiveData<Boolean> = _dataLoading
    val currentCityList: LiveData<List<City>> = _currentCityList

    init {
        _currentCityList.addSource(_searchCityName) {
            getCityListByName(it)
        }
    }

    private fun getCityListByName(query: String): LiveData<List<City>> {
        return citiesRepository.searchCityByName(query)
    }

    fun stop() {
        compositeDisposable.clear()
    }

    fun searchCity(query: String) {
        _searchCityName.value = query
    }
}

private const val TAG = "SearchCityViewModel"
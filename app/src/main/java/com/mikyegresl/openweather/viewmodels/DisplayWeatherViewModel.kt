package com.mikyegresl.openweather.viewmodels

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.*
import com.mikyegresl.openweather.data.CitiesRepository
import com.mikyegresl.openweather.data.model.Point
import com.mikyegresl.openweather.data.WeatherRepository
import com.mikyegresl.openweather.data.model.City
import com.mikyegresl.openweather.data.model.Weather
import com.mikyegresl.openweather.data.model.WeatherResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import javax.inject.Inject

class DisplayWeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val citiesRepository: CitiesRepository
): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _dataLoading = MutableLiveData<Boolean>()
    private val _citiesInserted = MutableLiveData<Boolean>()
    private val _currentLocation = MutableLiveData<Point>()
    private val _currentCityId = MediatorLiveData<String>()
    private val _weatherData = MediatorLiveData<WeatherResult>()

    val dataLoading: LiveData<Boolean> = _dataLoading
    val citiesInserted: LiveData<Boolean> = _citiesInserted

    init {
        _currentCityId.addSource(_currentLocation) {
            getCityId(it).value?.id
        }

        _weatherData.addSource(_currentCityId) {
            fetchWeatherData(it)
        }
    }

    fun stop() {
        compositeDisposable.clear()
    }

    private fun getCityId(location: Point): LiveData<City> {
        return citiesRepository.searchCityByLocation(location)
    }

    private fun fetchWeatherData(cityId: String) {
        _dataLoading.value = true
        compositeDisposable.add(
            weatherRepository.fetchWeatherData(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    _dataLoading.value = false
                }
                .doOnNext {
                    when (it) {
                        is WeatherResult.Success -> {
                            saveWeatherData(it.weather)
                        }
                        is WeatherResult.Failure -> {
                            _weatherData.value = WeatherResult.Failure
                        }
                    }
                }
                .doOnError {
                    _weatherData.value = WeatherResult.Failure
                }
                .subscribe()
        )
    }

    private fun saveWeatherData(weatherData: Weather) {
        compositeDisposable.add(
            weatherRepository.saveWeatherData(weatherData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    if (it is SQLiteException) {
                        throw it
                    }
                }
                .subscribe()
        )
    }

    fun saveCities(citiesInserted: Boolean, inputStream: InputStream) {
        if (!citiesInserted) {
            _dataLoading.value = true

            compositeDisposable.add(
                citiesRepository.insertCities(inputStream)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        _dataLoading.value = false
                        _citiesInserted.value = true
                    }
                    .doOnError {
                        _dataLoading.value = false
                        _citiesInserted.value = false
                    }
                    .subscribe()
            )
        }
        _citiesInserted.value = true
    }

    fun addToFavorites() {
        if (_currentCityId.value == null) {
            return
        }
        _dataLoading.value = true
        compositeDisposable.add(
            citiesRepository.addToFavorites(_currentCityId.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    _dataLoading.value = false
                }
                .subscribe()
        )
    }

    fun removeFromFavorites() {
        if (_currentCityId.value == null) {
            return
        }
        _dataLoading.value = true
        compositeDisposable.add(
            citiesRepository.removeFromFavorites(_currentCityId.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    _dataLoading.value = false
                }
                .subscribe()
        )
    }

    fun updateCurrentLocation(location: Point) {
        _currentLocation.value = location
    }

    fun getCityData(): LiveData<City> {
        return Transformations.switchMap(_currentCityId) {
            citiesRepository.searchCityById(it)
        }
    }

    fun getWeatherData(): LiveData<Weather> {
        return Transformations.switchMap(_currentCityId) {
            weatherRepository.observeWeatherData(it)
        }
    }
}
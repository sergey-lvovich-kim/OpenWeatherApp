package com.mikyegresl.openweather.viewmodels

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.*
import com.mikyegresl.openweather.data.CitiesRepository
import com.mikyegresl.openweather.data.model.Point
import com.mikyegresl.openweather.data.WeatherRepository
import com.mikyegresl.openweather.data.local.Prefs
import com.mikyegresl.openweather.data.model.City
import com.mikyegresl.openweather.data.model.Weather
import com.mikyegresl.openweather.data.model.WeatherResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import javax.inject.Inject

class DisplayWeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val citiesRepository: CitiesRepository,
    private val prefs: Prefs
): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _dataLoading = MutableLiveData<Boolean>()
    private val _currentLocation = MutableLiveData<Point>()
    private val _currentCityId = MutableLiveData<String>()

    private val _currentCity = MediatorLiveData<City>()
    private val _fetchWeatherResult = MediatorLiveData<WeatherResult>()
//    private val _savedWeatherData = MediatorLiveData<Weather>()

    val dataLoading: LiveData<Boolean> = _dataLoading
    val fetchWeatherResult: LiveData<WeatherResult> = _fetchWeatherResult
    val currentCity: LiveData<City> = _currentCity
//    val savedWeatherData: LiveData<Weather> = _savedWeatherData

    init {
        _dataLoading.value = false
        _fetchWeatherResult.addSource(_currentLocation) { location -> fetchWeatherData(location) }
        _currentCity.addSource(_currentCityId) { cityId -> getCityById(cityId) }
//        _savedWeatherData.addSource(_currentCityId) { cityId -> getSavedWeatherData(cityId) }
    }

    private fun fetchWeatherData(location: Point) {
        _dataLoading.value = true
        compositeDisposable.add(
            weatherRepository.fetchWeatherData(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        when (result) {
                            is WeatherResult.Success -> {
                                _fetchWeatherResult.value = WeatherResult.Success(result.weather)
                                _currentCityId.value = result.weather.cityId
                                Log.i(TAG, "currentCityId=${_currentCityId.value}")
                            }
                            is WeatherResult.Failure -> {
                                _fetchWeatherResult.value = WeatherResult.Failure
                            }
                        }
                    },
                    { throwable ->
                        _dataLoading.value = false
                        _fetchWeatherResult.value = WeatherResult.Failure
                        Log.e(TAG, "fetchWeatherData():", throwable)
                    },
                    {
                        _dataLoading.value = false
                        Log.i(TAG, "fetchWeatherData(): completed")
                    }
                )
        )
    }

    private fun getCityById(cityId: String) {
        compositeDisposable.add(
            citiesRepository.searchCityById(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { city ->
                        _currentCity.value = city
                    },
                    { throwable ->
                        Log.e(TAG, "getCityById():", throwable)
                    },
                    {
                        Log.i(TAG, "getCityById(): completed")
                    }
                )
        )
    }

    private fun getSavedWeatherData(cityId: String): LiveData<Weather> {
        return weatherRepository.observeWeatherData(cityId)
    }

    fun stop() {
        compositeDisposable.clear()
    }

    fun saveCities(inputStream: InputStream) {
        val citiesInserted = prefs.getBoolean(Prefs.CITIES_INSERTED)

        if (!citiesInserted) {
            _dataLoading.value = true

            compositeDisposable.add(
                citiesRepository.insertCities(inputStream)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        _dataLoading.value = false
                        prefs.putBoolean(Prefs.CITIES_INSERTED, true)

                    }, { throwable ->
                        _dataLoading.value = false
                        prefs.putBoolean(Prefs.CITIES_INSERTED, false)
                        Log.e(TAG, "saveCities():", throwable)
                    })
            )
        }
    }

//    private fun fetchWeatherData(cityId: String) {
//        _dataLoading.value = true
//        compositeDisposable.add(
//            weatherRepository.fetchWeatherData(cityId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { result ->
//                        when (result) {
//                            is WeatherResult.Success -> {
//                                _weatherData.value = result.weather
//                            }
//                            is WeatherResult.Failure -> {
//
//                            }
//                        }
//                    },
//                    { throwable ->
//                        _weatherData.value = WeatherResult.Failure
//                        Log.e(TAG, "fetchWeatherData():", throwable)
//                    },
//                    {
//                        _dataLoading.value = false
//                        Log.i(TAG, "fetchWeatherData(): completed")
//                    }
//                )
//        )
//    }

    fun refreshWeatherData() {
//        val cityId = _currentCityId.value
//
//        if (cityId != null) {
//            fetchWeatherData(cityId)
//        }
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
}

private const val TAG = "DisplayWeatherViewModel"
package com.mikyegresl.openweather.data

import androidx.lifecycle.LiveData
import com.mikyegresl.openweather.data.model.City
import com.mikyegresl.openweather.data.model.Point
import io.reactivex.Completable
import io.reactivex.Observable
import java.io.InputStream

interface CitiesRepository {
    fun observeCities(): LiveData<List<City>>
    fun addToFavorites(cityId: String): Completable
    fun removeFromFavorites(cityId: String): Completable
    fun insertCities(inputStream: InputStream): Completable
    fun searchCityByName(name: String): LiveData<List<City>>
    fun searchCityByLocation(location: Point): LiveData<City>
    fun searchCityById(id: String): LiveData<City>
}
package com.mikyegresl.openweather.data

import androidx.lifecycle.LiveData
import com.mikyegresl.openweather.data.model.City
import com.mikyegresl.openweather.data.model.Point
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.io.InputStream

interface CitiesRepository {
    fun addToFavorites(cityId: String): Completable
    fun removeFromFavorites(cityId: String): Completable
    fun insertCities(inputStream: InputStream): Completable
    fun searchCityById(id: String): Observable<City>
    fun observeCities(): LiveData<List<City>>
    fun searchCityByName(name: String): LiveData<List<City>>
}
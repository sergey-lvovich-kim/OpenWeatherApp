package com.mikyegresl.openweather.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.mikyegresl.openweather.data.local.CitiesDao
import com.mikyegresl.openweather.data.local.Database
import com.mikyegresl.openweather.data.model.City
import com.mikyegresl.openweather.data.model.Point
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

class CitiesRepositoryImpl @Inject constructor(
    private val citiesDao: CitiesDao
): CitiesRepository {
    override fun observeCities(): LiveData<List<City>> {
        return citiesDao.observeCities()
    }

    override fun addToFavorites(cityId: String): Completable {
        return citiesDao.setCityFavorite(cityId, true)
    }

    override fun removeFromFavorites(cityId: String): Completable {
        return citiesDao.setCityFavorite(cityId, false)
    }

    override fun insertCities(inputStream: InputStream): Completable {
        return Completable.create {
            val cityType = object: TypeToken<City>() {}.type
            val tempBuffer = mutableListOf<City>()
            val gson = Gson()
            var reader: JsonReader? = null

            try {
                reader = JsonReader(
                    InputStreamReader(inputStream)
                )
                reader.beginArray()

                while (reader.hasNext()) {
                    tempBuffer.add(gson.fromJson(reader, cityType))

                    if (tempBuffer.size > Database.BUFFER_SIZE) {
                        citiesDao.insertCities(tempBuffer)
                        tempBuffer.clear()
                    }
                }
            }
            finally {
                tempBuffer.clear()
                reader?.close()
            }
        }
    }

    override fun searchCityByName(name: String): LiveData<List<City>> {
        return citiesDao.searchCityByName(name)
    }

    override fun searchCityByLocation(location: Point): LiveData<City> {
        return citiesDao.searchCityByLocation(location)
    }

    override fun searchCityById(id: String): LiveData<City> {
        return citiesDao.searchCityById(id)
    }

}
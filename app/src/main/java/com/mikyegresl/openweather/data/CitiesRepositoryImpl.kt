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
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

class CitiesRepositoryImpl @Inject constructor(
    private val citiesDao: CitiesDao
): CitiesRepository {
    private val gson = Gson()
    private val cityType = object: TypeToken<City>() {}.type
    private lateinit var reader: JsonReader

    private fun readCitiesFromFile(inputStream: InputStream): Observable<List<City>> {
        return Observable.create { subscriber ->
            val tempBuffer = mutableListOf<City>()

            try {
                reader = JsonReader(InputStreamReader(inputStream))
                reader.beginArray()

                while (reader.hasNext()) {
                    tempBuffer.add(gson.fromJson(reader, cityType))

                    if (tempBuffer.size >= Database.BUFFER_SIZE) {
                        subscriber.onNext(tempBuffer)
                        tempBuffer.clear()
                    }
                }
            }
            catch (t: Throwable) {
                subscriber.onError(t)
            }
            finally {
                tempBuffer.clear()
                reader.close()
                subscriber.onComplete()
            }
        }
    }

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
        return readCitiesFromFile(inputStream)
            .flatMapCompletable { cityList ->
                citiesDao.insertCities(cityList)
            }
    }

    override fun searchCityByName(name: String): LiveData<List<City>> {
        return citiesDao.searchCityByName(name)
    }

    override fun searchCityById(id: String): Observable<City> {
        return citiesDao.searchCityById(id).toObservable()
    }
}
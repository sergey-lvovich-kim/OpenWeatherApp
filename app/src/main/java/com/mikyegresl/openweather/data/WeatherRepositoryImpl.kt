package com.mikyegresl.openweather.data

import androidx.lifecycle.LiveData
import com.mikyegresl.openweather.data.local.WeatherDao
import com.mikyegresl.openweather.data.model.Point
import com.mikyegresl.openweather.data.model.Weather
import com.mikyegresl.openweather.data.model.WeatherResult
import com.mikyegresl.openweather.data.remote.OpenWeatherApi
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.CancellationException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val weatherApi: OpenWeatherApi):  WeatherRepository {

    private fun saveWeatherData(weather: Weather): Completable {
        return weatherDao.insertWeatherData(weather)
    }

    override fun fetchWeatherData(cityId: String): Observable<WeatherResult> {
        //todo: probably need to change implementation
        return weatherApi.fetchCurrentWeatherDataByCityId(cityId)
            .map {
                val weatherResponse = it.body()
                if (it.isSuccessful && weatherResponse != null) {
                    val weather = Weather(
                        weatherResponse.weatherSchema[0].id,
                        weatherResponse.weatherSchema[0].weather,
                        weatherResponse.weatherSchema[0].description,
                        weatherResponse.weatherDatumSchema.temperature,
                        weatherResponse.weatherDatumSchema.humidity,
                        weatherResponse.weatherDatumSchema.pressure,
                        weatherResponse.cityId
                    )
                    return@map WeatherResult.Success(weather)
                }
                else {
                    return@map WeatherResult.Failure
                }
            }
            .onErrorReturn {
                if (it !is CancellationException) {
                    return@onErrorReturn WeatherResult.Failure
                }
                throw it
            }
            .toObservable()
    }

    override fun fetchWeatherData(location: Point): Observable<WeatherResult> {
        return weatherApi.fetchCurrentWeatherDataByCoordinates(location.longitude, location.latitude)
            .flatMapObservable { response ->
                val weatherResponse = response.body()

                if (response.isSuccessful && weatherResponse != null) {
                    val weather = Weather(
                        weatherResponse.weatherSchema[0].id,
                        weatherResponse.weatherSchema[0].weather,
                        weatherResponse.weatherSchema[0].description,
                        weatherResponse.weatherDatumSchema.temperature,
                        weatherResponse.weatherDatumSchema.humidity,
                        weatherResponse.weatherDatumSchema.pressure,
                        weatherResponse.cityId
                    )
                    return@flatMapObservable saveWeatherData(weather)
                        .andThen(Observable.just(WeatherResult.Success(weather)))
                }
                return@flatMapObservable Observable.just(WeatherResult.Failure)
            }
    }

    override fun observeWeatherData(cityId: String): LiveData<Weather> {
        return weatherDao.observeWeatherDataByCityId(cityId)
    }
}
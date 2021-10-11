package com.mikyegresl.openweather.data.remote

import com.mikyegresl.openweather.Constants
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("weather?appid=${Constants.API_KEY}&units=metric")
    fun fetchCurrentWeatherDataByCityId(
        @Query("q") cityId: String): Single<Response<WeatherDataResponseSchema>>
}
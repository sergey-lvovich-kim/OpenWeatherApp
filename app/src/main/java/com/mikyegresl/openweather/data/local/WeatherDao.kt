package com.mikyegresl.openweather.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikyegresl.openweather.data.model.Weather
import io.reactivex.Completable

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(weather: Weather): Completable

    @Query("SELECT * FROM weather WHERE city_id == :cityId")
    fun observeWeatherDataByCityId(cityId: String): LiveData<Weather>
}
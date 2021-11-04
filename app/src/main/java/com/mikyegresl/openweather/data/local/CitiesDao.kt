package com.mikyegresl.openweather.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikyegresl.openweather.data.model.City
import com.mikyegresl.openweather.data.model.Point
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CitiesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCities(cities: List<City>): Completable

    @Query("SELECT * FROM cities ORDER BY name ASC")
    fun observeCities(): LiveData<List<City>>

    @Query("UPDATE cities SET is_favorite = :isFavorite WHERE id == :id")
    fun setCityFavorite(id: String, isFavorite: Boolean): Completable

    @Query("SELECT * FROM cities WHERE name LIKE '%' || :name || '%'")
    fun searchCityByName(name: String): LiveData<List<City>>

    @Query("SELECT * FROM cities WHERE id LIKE '%' || :id || '%' LIMIT 1")
    fun searchCityById(id: String): Single<City>

    @Query("SELECT * FROM cities WHERE point == :point LIMIT 1")
    fun searchCityByLocation(point: Point): LiveData<City>

    @Query("SELECT id FROM cities WHERE point == :point LIMIT 1")
    fun searchCityIdByLocation(point: Point): LiveData<String>
}
package com.mikyegresl.openweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikyegresl.openweather.data.model.City
import com.mikyegresl.openweather.data.model.Weather
import com.mikyegresl.openweather.data.local.converters.PointConverter

@Database(
    entities = [City::class, Weather::class], version = 1, exportSchema = false,
)
@TypeConverters(PointConverter::class)
abstract class Database: RoomDatabase() {
    abstract fun citiesDao(): CitiesDao
    abstract fun weatherDao(): WeatherDao

    companion object {
        const val DB_NAME = "open_weather.db"
        const val CITIES_FILENAME = "cities.json"
        const val BUFFER_SIZE = 1024
    }
}
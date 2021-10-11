package com.mikyegresl.openweather.data.model

import androidx.room.*

@Entity(
    tableName = "weather",
    foreignKeys = [
        ForeignKey(
            entity = City::class,
            parentColumns = ["id"],
            childColumns = ["city_id"]
        )
    ],
    indices = [
        Index(value = ["city_id"])
    ]
)
data class Weather(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "weather") val weather: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "temperature") val temperature: Double,
    @ColumnInfo(name = "humidity") val humidity: Double,
    @ColumnInfo(name = "pressure") val pressure: Double,
    @ColumnInfo(name = "city_id") val cityId: String


)
package com.mikyegresl.openweather.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mikyegresl.openweather.data.model.Point

data class WeatherDataResponseSchema(
    @Expose @SerializedName("coord") val point: Point,
    @Expose @SerializedName("id") val cityId: String,
    @Expose @SerializedName("name") val cityName: String,
    @Expose @SerializedName("weather") val weatherSchema: List<WeatherSchema> = ArrayList(0),
    @Expose @SerializedName("main") val weatherDatumSchema: WeatherDataSchema
) {
    data class WeatherSchema(
        @Expose @SerializedName("id") val id: String,
        @Expose @SerializedName("main") val weather: String,
        @Expose @SerializedName("description") val description: String,
        @Expose @SerializedName("icon") val iconUrl: String
    )

    data class WeatherDataSchema(
        @Expose @SerializedName("temp") val temperature: Double,
        @Expose @SerializedName("pressure") val pressure: Double,
        @Expose @SerializedName("humidity") val humidity: Double
    )
}
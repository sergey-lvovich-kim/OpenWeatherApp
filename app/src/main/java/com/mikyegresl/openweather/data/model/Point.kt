package com.mikyegresl.openweather.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Point(
    @Expose @SerializedName("lon") var longitude: Double = 0.0,
    @Expose @SerializedName("lat") var latitude: Double = 0.0
)
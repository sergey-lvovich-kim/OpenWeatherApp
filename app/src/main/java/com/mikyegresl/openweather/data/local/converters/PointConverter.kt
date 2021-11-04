package com.mikyegresl.openweather.data.local.converters

import android.util.Log
import androidx.room.TypeConverter
import com.mikyegresl.openweather.data.model.Point

class PointConverter {
    @TypeConverter
    fun fromPoint(value: String): Point {
        return Point(
            value.substring(0, value.indexOf(";")).toDouble(),
            value.substring(value.indexOf(";") + 1).toDouble())
    }

    @TypeConverter
    fun toPoint(value: Point): String {
        return "${value.longitude};${value.latitude}"
    }
}
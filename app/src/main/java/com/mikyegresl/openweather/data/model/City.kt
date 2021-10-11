package com.mikyegresl.openweather.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cities")
data class City(
    @Expose @SerializedName("id") @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @Expose @SerializedName("name") @ColumnInfo(name = "name") val name: String,
    @Expose @SerializedName("state") @ColumnInfo(name = "state") val state: String,
    @Expose @SerializedName("country") @ColumnInfo(name = "country") val country: String,
    @Expose @SerializedName("coord") @ColumnInfo(name = "point") val point: Point,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false
)

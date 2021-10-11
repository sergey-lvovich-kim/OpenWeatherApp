package com.mikyegresl.openweather.data.local

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class Prefs(
    application: Application
) {
    private val sharedPrefs: SharedPreferences = application.getSharedPreferences(
        application.packageName + PREFERENCES_FILENAME,
        Context.MODE_PRIVATE
    )

    fun putBoolean(key: String, value: Boolean) {
        sharedPrefs.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean = sharedPrefs.getBoolean(key, false)

    companion object {
        const val PREFERENCES_FILENAME = "default_prefs"
        const val CITIES_INSERTED = "cities_inserted"
    }
}
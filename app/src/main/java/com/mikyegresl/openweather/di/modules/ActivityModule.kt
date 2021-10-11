package com.mikyegresl.openweather.di.modules

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.mikyegresl.openweather.utils.LocationManager
import com.mikyegresl.openweather.utils.LocationPermissionManager
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {
    @Provides
    fun activity() = activity

    @Provides
    fun fragmentManager() = activity.supportFragmentManager

    @Provides
    fun layoutInflater() = LayoutInflater.from(activity)!!

    @Provides
    fun permissionManager() = LocationPermissionManager(activity)

    @Provides
    fun locationManager() = LocationManager(activity)
}
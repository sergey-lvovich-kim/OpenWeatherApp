package com.mikyegresl.openweather.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import javax.inject.Inject

class LocationManager @Inject constructor(
    activity: AppCompatActivity
) {
    private val locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

    @SuppressLint("MissingPermission")
    fun bindListener(listener: OnCompleteListener<Location>) {
        locationService.lastLocation.addOnCompleteListener(listener)
    }
}
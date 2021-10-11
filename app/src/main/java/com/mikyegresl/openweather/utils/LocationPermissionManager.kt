package com.mikyegresl.openweather.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity

class LocationPermissionManager(
    private val activity: AppCompatActivity
) {
    enum class PermissionEvent {
        LOCATION_DISABLED,
        PERMISSION_DENIED,
        PERMISSION_GRANTED,
        ACCESS_DENIED
    }

    private val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun checkLocationPermission(): PermissionEvent {
        when (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            PERMISSION_GRANTED -> {
                if (!isLocationEnabled()) {
                    return PermissionEvent.LOCATION_DISABLED
                }
                return PermissionEvent.PERMISSION_GRANTED
            }
            PERMISSION_DENIED -> {
                if (activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    return PermissionEvent.ACCESS_DENIED
                }
                return PermissionEvent.PERMISSION_DENIED
            }
        }
        return PermissionEvent.ACCESS_DENIED
    }

    fun requestLocationPermission() {
        activity.requestPermissions(permissionArray, ACCESS_LOCATION_REQUEST_CODE)
    }

    fun processPermissionResult(requestCode: Int,
                                grantResults: IntArray): Boolean {
        if (requestCode != requestCode || grantResults.isEmpty()) {
            return false
        }
        for (result in grantResults) {
            if (result == PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    companion object {
        const val ACCESS_LOCATION_REQUEST_CODE = 0x101
        val permissionArray = Array(1) {
            Manifest.permission.ACCESS_COARSE_LOCATION
        }
    }
}
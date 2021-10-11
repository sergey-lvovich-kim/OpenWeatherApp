package com.mikyegresl.openweather.view.screens

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.mikyegresl.openweather.R
import com.mikyegresl.openweather.data.model.Point
import com.mikyegresl.openweather.utils.LocationManager
import com.mikyegresl.openweather.utils.LocationPermissionManager
import com.mikyegresl.openweather.view.common.BaseActivity
import com.mikyegresl.openweather.view.screens.dialogs.DialogsNavigator
import com.mikyegresl.openweather.viewmodels.DisplayWeatherViewModel
import com.mikyegresl.openweather.viewmodels.common.ViewModelFactory
import javax.inject.Inject

class MainActivity : BaseActivity(), OnCompleteListener<Location> {
    @Inject lateinit var dialogsNavigator: DialogsNavigator
    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var locationPermissionManager: LocationPermissionManager
    @Inject lateinit var viewModelFactory: ViewModelFactory

    private lateinit var weatherViewModel: DisplayWeatherViewModel
//    private lateinit var citiesViewModel: CitiesViewModel

    private var location = Point()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presentationComponent.inject(this)

        weatherViewModel = ViewModelProvider(this, viewModelFactory).get(DisplayWeatherViewModel::class.java)
//        citiesViewModel = ViewModelProvider(this, viewModelFactory).get(CitiesViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()



        when (locationPermissionManager.checkLocationPermission()) {
            LocationPermissionManager.PermissionEvent.LOCATION_DISABLED ->
                dialogsNavigator.showGpsNecessityDialog()
            LocationPermissionManager.PermissionEvent.PERMISSION_DENIED ->
                locationPermissionManager.requestLocationPermission()
            LocationPermissionManager.PermissionEvent.PERMISSION_GRANTED ->
                locationManager.bindListener(this)
            else -> {
                //do nothing for now
            }
        }
    }

    override fun onStop() {
        super.onStop()

        weatherViewModel.stop()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (locationPermissionManager.processPermissionResult(requestCode, grantResults)) {
            locationManager.bindListener(this)
        }
        else {
            dialogsNavigator.showPermissionNecessityDialog(
                LocationPermissionManager.ACCESS_LOCATION_REQUEST_CODE,
                LocationPermissionManager.permissionArray)
        }
    }

    override fun onComplete(task: Task<Location>) {
        location.latitude = task.result.latitude
        location.longitude = task.result.longitude
        weatherViewModel.updateCurrentLocation(location)
    }
}
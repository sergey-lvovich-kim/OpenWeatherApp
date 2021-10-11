package com.mikyegresl.openweather.view.screens.dialogs

import androidx.fragment.app.FragmentManager
import javax.inject.Inject

class DialogsNavigator @Inject constructor(
    private val fragmentManager: FragmentManager
) {
    fun showPermissionNecessityDialog(requestCode: Int, permissionList: Array<String>) {
        fragmentManager
            .beginTransaction()
            .add(PermissionNeedExplanatoryDialog.newInstance(requestCode, permissionList), null)
            .commitAllowingStateLoss()
    }

    fun showGpsNecessityDialog() {
        fragmentManager
            .beginTransaction()
            .add(GpsNecessityDialog.newInstance(), null)
            .commitAllowingStateLoss()
    }

    fun showApiErrorDialog() {
        fragmentManager
            .beginTransaction()
            .add(ServerErrorDialog.newInstance(), null)
            .commitAllowingStateLoss()
    }
}
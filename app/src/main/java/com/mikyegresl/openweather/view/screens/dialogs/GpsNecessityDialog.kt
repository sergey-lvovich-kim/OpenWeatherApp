package com.mikyegresl.openweather.view.screens.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.DialogFragment
import com.mikyegresl.openweather.R

class GpsNecessityDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity).let {
            it.setTitle(R.string.gps_need_explanator_title)
            it.setMessage(R.string.gps_need_explanatory_dialog_message)
            it.setPositiveButton(R.string.open_settings_btn_text) { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                println("trigger")
            }
            it.create()
        }
    }
    companion object {
        fun newInstance(): GpsNecessityDialog = GpsNecessityDialog()
    }
}
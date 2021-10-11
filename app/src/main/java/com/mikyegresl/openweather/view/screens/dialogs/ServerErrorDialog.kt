package com.mikyegresl.openweather.view.screens.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mikyegresl.openweather.R

class ServerErrorDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity).let {
            it.setTitle(R.string.api_error_title)
            it.setMessage(R.string.api_error_message)
            it.setPositiveButton(R.string.dialog_positive_btn_text) { _, _ -> dismiss()}
            it.create()
        }
    }
    companion object {
        fun newInstance(): ServerErrorDialog = ServerErrorDialog()
    }
}
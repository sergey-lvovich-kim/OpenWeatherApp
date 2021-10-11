package com.mikyegresl.openweather.view.screens.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mikyegresl.openweather.R
import java.lang.IllegalArgumentException

class PermissionNeedExplanatoryDialog: DialogFragment() {
    private var permissionArray: Array<String>? = null
    private var requestCode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestCode = requireArguments().getInt(ARG_REQUEST_CODE)
        permissionArray = requireArguments().getStringArray(ARG_PERMISSION_ARRAY)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity).let {
            it.setTitle(R.string.permission_need_explanatory_dialog_title)
            it.setMessage(R.string.permission_need_explanatory_dialog_message)
            it.setPositiveButton(R.string.dialog_positive_btn_text) { _, _ ->
                if (permissionArray != null) {
                    requireActivity().requestPermissions(permissionArray!!, requestCode)
                }
                else {
                    throw IllegalArgumentException()
                }
            }
            it.create()
        }
    }

    companion object {
        private const val ARG_REQUEST_CODE = "requestCode"
        private const val ARG_PERMISSION_ARRAY = "permissionArray"

        fun newInstance(requestCode: Int, permissionArray: Array<String>): PermissionNeedExplanatoryDialog =
            PermissionNeedExplanatoryDialog().apply {
                arguments = Bundle().apply {
                    putInt(ARG_REQUEST_CODE, requestCode)
                    putStringArray(ARG_PERMISSION_ARRAY, permissionArray)
                }
            }
    }
}
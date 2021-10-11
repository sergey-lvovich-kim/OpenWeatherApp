package com.mikyegresl.openweather.view.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

open class BaseViewContainer<LISTENER>(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    @LayoutRes layoutRes: Int
) {
    protected val listeners = HashSet<LISTENER>()
    val rootView: View = layoutInflater.inflate(layoutRes, viewGroup, false)
    private val context: Context = layoutInflater.context

    fun registerListener(listener: LISTENER) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: LISTENER) {
        listeners.remove(listener)
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun <T: View> findViewById(@IdRes idRes: Int): T = rootView.findViewById<T>(idRes)
}
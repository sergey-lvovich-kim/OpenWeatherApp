package com.mikyegresl.openweather.view.common

import androidx.fragment.app.Fragment
import com.mikyegresl.openweather.di.components.PresentationComponent

open class BaseFragment: Fragment() {
    protected val presentationComponent: PresentationComponent by lazy {
        (requireActivity() as BaseActivity).presentationComponent
    }
}
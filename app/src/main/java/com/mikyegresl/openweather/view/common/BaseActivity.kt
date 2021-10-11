package com.mikyegresl.openweather.view.common

import androidx.appcompat.app.AppCompatActivity
import com.mikyegresl.openweather.OpenWeatherApp
import com.mikyegresl.openweather.di.components.ActivityComponent
import com.mikyegresl.openweather.di.components.AppComponent
import com.mikyegresl.openweather.di.components.PresentationComponent
import com.mikyegresl.openweather.di.modules.ActivityModule
import com.mikyegresl.openweather.di.modules.PresentationModule

open class BaseActivity: AppCompatActivity() {
    private val appComponent: AppComponent get() = (application as OpenWeatherApp).appComponent

    private val activityComponent: ActivityComponent by lazy {
        appComponent.newActivityComponent(ActivityModule(this))
    }

    val presentationComponent: PresentationComponent by lazy {
        activityComponent.newPresentationComponent(PresentationModule())
    }
}
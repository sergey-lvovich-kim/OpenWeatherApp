package com.mikyegresl.openweather

import android.app.Application
import com.mikyegresl.openweather.di.AppModule
import com.mikyegresl.openweather.di.components.AppComponent
import com.mikyegresl.openweather.di.components.DaggerAppComponent

class OpenWeatherApp: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }
}
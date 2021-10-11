package com.mikyegresl.openweather.di.components

import com.mikyegresl.openweather.di.AppModule
import com.mikyegresl.openweather.di.modules.ActivityModule
import com.mikyegresl.openweather.di.scopes.AppScope
import dagger.Component

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {
    fun newActivityComponent(activityModule: ActivityModule): ActivityComponent
}
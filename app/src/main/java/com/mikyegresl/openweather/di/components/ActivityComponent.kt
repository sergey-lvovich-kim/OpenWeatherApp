package com.mikyegresl.openweather.di.components

import com.mikyegresl.openweather.di.modules.ActivityModule
import com.mikyegresl.openweather.di.modules.PresentationModule
import com.mikyegresl.openweather.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun newPresentationComponent(presentationModule: PresentationModule): PresentationComponent
}
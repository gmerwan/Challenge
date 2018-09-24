package com.wunder.challenge.injection.component

import com.wunder.challenge.injection.module.NetworkModule
import com.wunder.challenge.ui.placemark.PlaceMarkListViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified PostListViewModel.
     * @param placeMarkListViewModel PlaceMarkListViewModel in which to inject the dependencies
     */
    fun inject(placeMarkListViewModel: PlaceMarkListViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}
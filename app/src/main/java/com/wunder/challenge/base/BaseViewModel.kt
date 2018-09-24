package com.wunder.challenge.base

import android.arch.lifecycle.ViewModel
import com.wunder.challenge.injection.component.DaggerViewModelInjector
import com.wunder.challenge.injection.component.ViewModelInjector
import com.wunder.challenge.injection.module.NetworkModule
import com.wunder.challenge.ui.placemark.PlaceMarkListViewModel

abstract class BaseViewModel:ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is PlaceMarkListViewModel -> injector.inject(this)
        }
    }
}
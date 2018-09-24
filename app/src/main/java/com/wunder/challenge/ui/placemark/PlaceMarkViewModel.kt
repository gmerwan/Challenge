package com.wunder.challenge.ui.placemark

import android.arch.lifecycle.MutableLiveData
import com.wunder.challenge.base.BaseViewModel
import com.wunder.challenge.model.PlaceMark

class PlaceMarkViewModel: BaseViewModel() {
    private val placeMarkAddress = MutableLiveData<String>()
    private val placeMarkCoordinates = MutableLiveData<List<Double>>()
    private val placeMarkEngineType = MutableLiveData<String>()
    private val placeMarkExterior = MutableLiveData<String>()
    private val placeMarkFuel = MutableLiveData<Int>()
    private val placeMarkInterior = MutableLiveData<String>()
    private val placeMarkName = MutableLiveData<String>()
    private val placeMarkVin = MutableLiveData<String>()

    fun bind(placeMark: PlaceMark){
        placeMarkAddress.value = placeMark.address
        placeMarkCoordinates.value = placeMark.coordinates
        placeMarkEngineType.value = placeMark.engineType
        placeMarkExterior.value = placeMark.exterior
        placeMarkFuel.value = placeMark.fuel
        placeMarkInterior.value = placeMark.interior
        placeMarkName.value = placeMark.name
        placeMarkVin.value = placeMark.vin
    }

    fun getPlaceMarkAddress():MutableLiveData<String>{
        return placeMarkAddress
    }

    fun getPlaceMarkCoordinates():MutableLiveData<List<Double>>{
        return placeMarkCoordinates
    }

    fun getPlaceMarkEngineType():MutableLiveData<String>{
        return placeMarkEngineType
    }

    fun getPlaceMarkExterior():MutableLiveData<String>{
        return placeMarkExterior
    }

    fun getPlaceMarkFuel():MutableLiveData<Int>{
        return placeMarkFuel
    }

    fun getPlaceMarkInterior():MutableLiveData<String>{
        return placeMarkInterior
    }

    fun getPlaceMarkName():MutableLiveData<String>{
        return placeMarkName
    }

    fun getPlaceMarkVin():MutableLiveData<String>{
        return placeMarkVin
    }
}